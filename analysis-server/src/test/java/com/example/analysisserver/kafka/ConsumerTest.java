package com.example.analysisserver.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsumerTest {


    @Test
    @KafkaListener(topics = "topic", containerFactory = "kafkaListenerContainerFactory")
    public void listener(ConsumerRecord<?, ?> record) {
        String key = (String) record.key();
        System.out.println(key);
    }


    @KafkaListener(containerFactory = "kafkaListenerContainerFactory",
            topicPartitions = @TopicPartition(topic = "topic", partitionOffsets = @PartitionOffset(partition = "0" , initialOffset = "2")))
    public void listener2() {

    }

}
