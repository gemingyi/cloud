//package com.example.pluginkafka.config;
//
//import com.example.pluginkafka.serialization.ObjectDeserializer;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableKafka
//@ConditionalOnProperty(prefix = "kafka-plugin-consumer" , havingValue = "true" , name = "batch")
//public class KafkaBatchConsumerAutoConfig {
//
//    private Map<String, Object> consumerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServers);
//        //是否自动提交偏移量，默认值是true,为了避免出现重复数据和数据丢失，可以把它设置为false,然后手动提交偏移量
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerEnableAutoCommit);
//        //consumer向zookeeper提交offset的时间间隔
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerAutoCommitInterval);
//        //zookeeper 会话的超时限制。如果consumer在这段时间内没有向zookeeper发送心跳信息，则它会被认为挂掉了，
//        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "8000");
//        //***一次从kafka中poll出来的数据条数***
//        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerMaxPollRecords);
//        //***max.poll.records条数据需要在max.poll.interval.ms这个时间内处理完 ***
//        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "100000");
//        //当zookeeper中没有初始的offset时候的处理方式 。smallest ：重置为最小值 largest:重置为最大值 anythingelse：抛出异常
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerAutoOffsetReset);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ObjectDeserializer.class);
//        return props;
//    }
//
//
//    @Bean
//    public ConsumerFactory consumerFactory() {
//        return new DefaultKafkaConsumerFactory(consumerConfigs());
//    }
//
//    @Bean
//    public KafkaListenerContainerFactory kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
//        factory.setConsumerFactory(consumerFactory());
//        // 控制多线程消费,并发数(如果topic有3各分区。设置成3，并发数就是3个线程，加快消费), 不设置setConcurrency就会变成单线程配置, MAX_POLL_RECORDS_CONFIG也会失效，接收的消息列表也不会是ConsumerRecord
//        factory.setConcurrency(1);
//        factory.getContainerProperties().setPollTimeout(3000);
//        // 设置为批量消费，每个批次数量在Kafka配置参数中设置（max.poll.records）
//        factory.setBatchListener(true);
//        return factory;
//    }
//
//}
