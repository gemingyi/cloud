package com.example.pluginkafka.config;

import com.example.pluginkafka.serialization.ObjectSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * https://www.imooc.com/article/286770
 */
@Configuration
@EnableKafka
@EnableConfigurationProperties({KafkaProperties.class})
@ConditionalOnProperty(name = {"spring.kafka.bootstrap-servers"})
public class KafkaProducerAutoConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaProducerAutoConfig(KafkaProperties properties) {
        this.kafkaProperties = properties;
    }


    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(16);
        //服务地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        //消息的确认模式 （0：不保证消息的到达确认 1：会等待leader 收到确认后, -1 all：等待leader收到确认，并进行复制操作后）
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        //消息发送失败后的重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getProducer().getRetries());
        //多久发一次 ms
        props.put(ProducerConfig.LINGER_MS_CONFIG, 500);
        //当有多个消息需要被发送到同一个分区时，生产者会把它们放在同一个批次里。该参数指定了一个批次可以使用的内存大小，按照字节数计算。
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 2000);
        //设置生产者内存缓冲区的大小。
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        //
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObjectSerializer.class);
        return props;
    }


    /**
     * 生产消息的工厂
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * kafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
