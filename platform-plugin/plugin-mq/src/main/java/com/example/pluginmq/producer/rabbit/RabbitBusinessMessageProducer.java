package com.example.pluginmq.producer.rabbit;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.commons.utils.AliJsonUtil;
import com.example.pluginmq.dao.entity.LocalMessage;
import com.example.pluginmq.dao.mapper.LocalMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class RabbitBusinessMessageProducer implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private LocalMessageMapper localMessageMapper;


    @PostConstruct
    private void init() {
        rabbitTemplate.setConfirmCallback(this);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean flag, String cause) {
        String messageId = correlationData != null ? correlationData.getId() : "";
        if (flag) {
            log.info("rabbitMQ confirm message success [messageId:{}]", messageId);
            // update local message table status
            LocalMessage entity = new LocalMessage();
            entity.setMessageStatus(1);
            entity.setUpdateTime(new Date());
            localMessageMapper.update(entity, new LambdaQueryWrapper<LocalMessage>().eq(LocalMessage::getMessageId, messageId));
        } else {
            log.error("rabbitMQ confirm message fail, [messageId:{}], [cause:{}]", messageId, cause);
            // update local message table status
            LocalMessage entity = new LocalMessage();
            entity.setMessageStatus(2);
            entity.setUpdateTime(new Date());
            localMessageMapper.update(entity, new LambdaQueryWrapper<LocalMessage>().eq(LocalMessage::getMessageId, messageId));
        }
    }


    /**
     * rabbitmq发送消息
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendBusinessMsg(String exchange, String routingKey, Object msg) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("rabbitMQ send message [exchange:{}], [routingKey:{}], [messageId:{}], [msg:{}]"
                , exchange, routingKey, correlationData.getId(), msg);

        // save local message table snatch
        LocalMessage entity = new LocalMessage();
        entity.setMqType(1);
        entity.setExchangeOrTopic(exchange);
        entity.setRoutingKeyOrPartition(routingKey);
        entity.setMessageId(correlationData.getId());
        entity.setMessageStatus(0);
        entity.setMessageData(AliJsonUtil.objectToJsonStr(msg));
        Date currentDate= new Date();
        entity.setCreateTime(currentDate);
        entity.setUpdateTime(currentDate);
        int result = localMessageMapper.insert(entity);

        //
        if (result > 0) {
            try {
                rabbitTemplate.convertAndSend(exchange, routingKey, msg, correlationData);
            } catch (Exception e) {
                log.error("rabbitMQ send message [exchange:{}], [routingKey:{}], [messageId:{}], [msg:{}], [e:{}]"
                        , exchange, routingKey, correlationData.getId(), msg, e);
            }
        }
    }

}
