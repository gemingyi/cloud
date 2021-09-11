package com.example.pluginmq.producer.kafka;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.commons.utils.AliJsonUtil;
import com.example.pluginmq.dao.entity.LocalMessage;
import com.example.pluginmq.dao.mapper.LocalMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;


@Slf4j
public class KafkaBusinessMessageProducer{

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private LocalMessageMapper localMessageMapper;


    @PostConstruct
    private void init() {
        kafkaTemplate.setProducerListener(new KafkaSendResultHandler());
    }


    public class KafkaSendResultHandler implements ProducerListener<String, Object> {
        @Override
        public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
            String key = producerRecord.key().toString();
            log.info("kafka confirm message success [topic:{}], [key:{}], [partition:{}], [msg:{}]",
                    producerRecord.topic(), key, producerRecord.partition(), producerRecord.value());
            //
            LocalMessage entity = new LocalMessage();
            entity.setMessageStatus(1);
            entity.setUpdateTime(new Date());
            localMessageMapper.update(entity, new LambdaQueryWrapper<LocalMessage>().eq(LocalMessage::getMessageId, key));
        }

        @Override
        public void onError(ProducerRecord producerRecord, Exception exception) {
            String key = producerRecord.key().toString();
            log.error("kafka confirm message fail [topic:{}], [key:{}], [partition:{}], [msg:{}], [cause:{}]",
                    producerRecord.topic(), key, producerRecord.partition(), producerRecord.value(), exception);
            //
            LocalMessage entity = new LocalMessage();
            entity.setMessageStatus(2);
            entity.setUpdateTime(new Date());
            localMessageMapper.update(entity, new LambdaQueryWrapper<LocalMessage>().eq(LocalMessage::getMessageId, key));
        }
    }


    /**
     * kafka发送消息
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendBusinessMsg(String topic, Integer partition, Object msg) {
        String uuid = UUID.randomUUID().toString();
        log.info("kafka send message [topic{}], [partition{}], [key:{}], [msg:{}]"
                , topic, partition, uuid, msg);

        // save local message table snatch
        LocalMessage entity = new LocalMessage();
        entity.setMqType(1);
        entity.setExchangeOrTopic(topic);
        entity.setRoutingKeyOrPartition(String.valueOf(partition));
        entity.setMessageId(uuid);
        entity.setMessageStatus(0);
        entity.setMessageData(AliJsonUtil.objectToJsonStr(msg));
        Date currentDate= new Date();
        entity.setCreateTime(currentDate);
        entity.setUpdateTime(currentDate);
        int result = localMessageMapper.insert(entity);

        //
        if (result > 0) {
            try {
                if (partition == null) {
                    kafkaTemplate.send(topic, uuid, msg);
                } else {
                    kafkaTemplate.send(topic, partition, uuid, msg);
                }
            } catch (Exception e) {
                log.info("kafka send message [topic{}], [partition{}], [key:{}], [msg:{}], [e:{}]"
                        , topic, partition, uuid, msg, e);
            }
        }


//        String uuid = UUID.randomUUID().toString();
////
//        log.info("kafka send message [topic{}], [partition{}], [routingKey:{}], [msg:{}]", topic, partition, uuid, msg);
//
//        // save local message table snatch
//        LocalMessage entity = new LocalMessage();
//        entity.setMqType(1);
//        entity.setExchangeOrTopic(topic);
//        entity.setRoutingKeyOrPartition(String.valueOf(partition));
//        entity.setMessageId(uuid);
//        entity.setMessageStatus(0);
//        entity.setMessageData(AliJsonUtil.objectToJsonStr(msg));
//        Date currentDate= new Date();
//        entity.setCreateTime(currentDate);
//        entity.setUpdateTime(currentDate);
//        int result = localMessageMapper.insert(entity);
//
//        if (result > 0) {
//            ListenableFuture<SendResult<String, Object>> sendResult;
//            if (StringUtils.isEmpty(partition)) {
//                sendResult = kafkaTemplate.send(topic, uuid, msg);
//            } else {
//                sendResult = kafkaTemplate.send(topic, partition, uuid, msg);
//            }
//            sendResult.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
//                @Override
//                public void onSuccess(SendResult<String, Object> sendResult) {
//                    ProducerRecord producerRecord = sendResult.getProducerRecord();
//                    String messageId = producerRecord.key().toString();
//
//                    log.info("kafka confirm message success [topic:{}], [routingKey:{}], [partition:{}], [msg:{}]",
//                            producerRecord.topic(), messageId, producerRecord.partition(), producerRecord.value());
//                    //
//                    LocalMessage entity = new LocalMessage();
//                    entity.setMessageStatus(1);
//                    entity.setUpdateTime(new Date());
//                    localMessageMapper.update(entity, new LambdaQueryWrapper<LocalMessage>().eq(LocalMessage::getMessageId, messageId));
//                }
//
//                @Override
//                public void onFailure(Throwable throwable) {
//                    log.info("kafka confirm message fail [topic:{}], [msg:{}], [cause:{}]", topic, msg, throwable);
//                }
//            });
//        }
    }

}
