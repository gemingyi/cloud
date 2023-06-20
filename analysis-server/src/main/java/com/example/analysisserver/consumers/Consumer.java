package com.example.analysisserver.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Consumer {

    @KafkaListener(topics = "topic1", groupId = "consumer1")
    public void listener(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        for (ConsumerRecord record : records) {
            String key = (String) record.key();
            System.out.println(key);
            System.out.println(record.value());
        }
        ack.acknowledge();
    }

//    @KafkaListener(topics = "topic1", containerFactory = "kafkaListenerContainerFactory", groupId = "consumer1")
//    public void listener(ConsumerRecord<?, ?> record) {
//        String key = (String) record.key();
////        System.out.println(String.format("key[%s]", key));
//        Notice notice = (Notice) record.value();
//        System.out.println(String.format("key[%s]:value[%s]", key, notice.getTitle()));
//    }

}
