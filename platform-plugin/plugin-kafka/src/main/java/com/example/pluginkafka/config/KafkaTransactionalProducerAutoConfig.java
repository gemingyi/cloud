//package com.example.pluginkafka.config;
//
//import com.example.pluginkafka.serialization.ObjectSerializer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.transaction.KafkaTransactionManager;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableKafka
//@ConditionalOnProperty(prefix = "kafka-plugin-producer" , havingValue = "true" , name = "transactional")
//public class KafkaTransactionalProducerAutoConfig {
//
//    /**
//     * 生产者配置
//     */
//    private Map<String, Object> producerConfigs() {
//        Map<String, Object> props = new HashMap<>(16);
//        //服务地址
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
//        //消息的确认模式 （0：不保证消息的到达确认 1：会等待leader 收到确认后, -1：等待leader收到确认，并进行复制操作后）
//        props.put(ProducerConfig.ACKS_CONFIG, producerAcks);
//        //消息发送失败后的重试次数
//        props.put(ProducerConfig.RETRIES_CONFIG, producerRetries);
//        //当有多个消息需要被发送到同一个分区时，生产者会把它们放在同一个批次里。该参数指定了一个批次可以使用的内存大小，按照字节数计算。
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, producerBatchSize);
//        //设置生产者内存缓冲区的大小。
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, producerBufferMemory);
//        //消息发送的最大大小 字节
//        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,producerMaxRequestSize);
//        //消息发送的最长等待时间
//        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,producerRequestTimeoutMs);
//        //
//        props.put(ProducerConfig.LINGER_MS_CONFIG, producerLingerMs);
//        //
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObjectSerializer.class);
//        return props;
//    }
//
//    /**
//     * 生产消息的工厂
//     */
//    @Bean
//    public ProducerFactory transactionalProducerFactory() {
//        DefaultKafkaProducerFactory factory = new DefaultKafkaProducerFactory(producerConfigs());
//        // 开启事务
//        factory.transactionCapable();
//        // 用来生成Transactional.id的前缀
//        factory.setTransactionIdPrefix("kafka-tran-");
//        return factory;
//    }
//
//    /**
//     * 事务管理器
//     */
//    @Bean
//    public KafkaTransactionManager transactionManager() {
//        KafkaTransactionManager manager = new KafkaTransactionManager(transactionalProducerFactory());
//        return manager;
//    }
//
//    /**
//     * kafkaTemplate
//     */
//    @Bean("transactionalTemplate")
//    public KafkaTemplate transactionalTemplate() {
//        KafkaTemplate template = new KafkaTemplate(transactionalProducerFactory());
//        return template;
//    }
//
//}
