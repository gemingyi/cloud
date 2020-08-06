package com.example.analysisserver.consumers;

import com.example.analysisserver.model.Notice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @KafkaListener(topics = "topic1", containerFactory = "kafkaListenerContainerFactory", groupId = "consumer1")
    public void listener(ConsumerRecord<?, ?> record) {
        String key = (String) record.key();
//        System.out.println(String.format("key[%s]", key));
        Notice notice = (Notice) record.value();
        System.out.println(String.format("key[%s]:value[%s]", key, notice.getTitle()));
    }

}
