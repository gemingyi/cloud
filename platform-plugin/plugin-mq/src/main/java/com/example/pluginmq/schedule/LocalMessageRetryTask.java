package com.example.pluginmq.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pluginmq.dao.entity.LocalMessage;
import com.example.pluginmq.dao.mapper.LocalMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class LocalMessageRetryTask {

    private static final int PAGE_SIZE = 50;

    @Autowired
    private LocalMessageMapper localMessageMapper;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Scheduled
    @Transactional(rollbackFor = RuntimeException.class)
    public void retryFailTask() {
        //
        LambdaQueryWrapper<LocalMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(LocalMessage::getMessageStatus, 1);
        long total = localMessageMapper.selectCount(wrapper);

        //
        long pageCount = total % PAGE_SIZE == 0 ? (total / PAGE_SIZE) : (total / PAGE_SIZE + 1);
        for (int currentPage = 1; currentPage <= pageCount; currentPage++) {
            IPage<LocalMessage> iPage = new Page<>();
            iPage.setCurrent(currentPage - 1);
            iPage.setPages(PAGE_SIZE);
            IPage<LocalMessage> pages = localMessageMapper.selectPage(iPage, wrapper);
            List<LocalMessage> localMessageList = pages.getRecords();
            List<LocalMessage> updateList = new ArrayList<>(localMessageList.size());
            for (LocalMessage message : localMessageList) {
                LocalMessage update = new LocalMessage();
                update.setId(message.getId());
                //
                if (message.getMqType() == 1) {
                    try {
                        log.info("kafka retry fail task send message [message:{}]",message);
                        if (StringUtils.isEmpty(message.getRoutingKeyOrPartition())) {
                            kafkaTemplate.send(message.getExchangeOrTopic(), message.getMessageId(), message.getMessageData());
                        } else {
                            kafkaTemplate.send(message.getExchangeOrTopic(), Integer.parseInt(message.getRoutingKeyOrPartition()), message.getMessageId(), message.getMessageData());
                        }
                        update.setMessageStatus(1);
                        updateList.add(update);
                    } catch (Exception e) {
                        log.error("kafka retry fail task send message [message:{}], [e:{}]",message, e);
                    }
                } else {
                    try {
                        log.info("rabbit retry fail task send message [message:{}]",message);
                        rabbitTemplate.convertAndSend(message.getExchangeOrTopic(), message.getRoutingKeyOrPartition(), message.getMessageData());
                        update.setMessageStatus(1);
                        updateList.add(update);
                    } catch (Exception e) {
                        log.error("rabbit retry fail task send message [message:{}], [e:{}]",message, e);
                    }
                }
            }

        }
    }

}
