package com.example.pluginmq.schedule;

import com.example.pluginmq.dao.entity.LocalMessage;
import com.example.pluginmq.dao.mapper.LocalMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class LocalMessageRetryTask {

    @Autowired
    private LocalMessageMapper localMessageMapper;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Scheduled
    @Transactional(rollbackFor = RuntimeException.class)
    public void retryFailTask() {
        long currentPage = 1;
        long pageSize = 20;

        while (true) {
            List<LocalMessage> localMessages = localMessageMapper.selectPage(currentPage - 1, pageSize);
            if (CollectionUtils.isEmpty(localMessages)) {
                break;
            }

            Date currentDate = new Date();
            List<LocalMessage> updateList = new ArrayList<>(localMessages.size());
            for (LocalMessage message : localMessages) {
                String exchangeOrTopic = message.getExchangeOrTopic();
                String routingKeyOrPartition = message.getRoutingKeyOrPartition();
                String messageData = message.getMessageData();

                //
                if (message.getMqType() == 1) {
                    try {
                        log.info("kafka retry fail task send message [topic:{}], [partition:{}], [key:{}], [message:{}]",
                                exchangeOrTopic, routingKeyOrPartition, messageData, message);
                        if (StringUtils.isEmpty(routingKeyOrPartition)) {
                            kafkaTemplate.send(exchangeOrTopic, messageData, message.getMessageData());
                        } else {
                            kafkaTemplate.send(exchangeOrTopic, Integer.parseInt(routingKeyOrPartition), messageData, message.getMessageData());
                        }
                        LocalMessage update = new LocalMessage();
                        update.setId(message.getId());
                        update.setRetryCount(message.getRetryCount() + 1);
                        update.setUpdateTime(currentDate);
                        updateList.add(update);
                    } catch (Exception e) {
                        log.error("kafka retry fail task send message [topic:{}], [partition:{}], [key:{}], [message:{}], [e:{}]",
                                exchangeOrTopic, routingKeyOrPartition, messageData, message, e);
                    }
                } else {
                    try {
                        log.info("rabbit retry fail task send message [exchange:{}], [routingKey:{}], [messageId:{}], [message:{}]",
                                exchangeOrTopic, routingKeyOrPartition, messageData, message);
                        rabbitTemplate.convertAndSend(exchangeOrTopic, routingKeyOrPartition, message.getMessageData());
                        LocalMessage update = new LocalMessage();
                        update.setId(message.getId());
                        update.setRetryCount(message.getRetryCount() + 1);
                        update.setUpdateTime(currentDate);
                        updateList.add(update);
                    } catch (Exception e) {
                        log.error("rabbit retry fail task send message [exchange:{}], [routingKey:{}], [messageId:{}], [message:{}], [e:{}]",
                                exchangeOrTopic, routingKeyOrPartition, messageData, message, e);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(updateList)) {
                localMessageMapper.updateBatch(updateList);
            }
        }
    }

}
