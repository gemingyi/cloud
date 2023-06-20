package com.example.pluginmq.config;

import com.example.pluginmq.producer.kafka.KafkaBusinessMessageProducer;
import com.example.pluginmq.producer.rabbit.RabbitBusinessMessageProducer;
import com.example.pluginmysql.config.MybatisPlusAutoConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@AutoConfigureAfter({MybatisPlusAutoConfig.class})
public class MQAutoConfig {

    @Bean
    @ConditionalOnProperty(prefix = "spring.kafka", name = "bootstrap-servers")
    public KafkaBusinessMessageProducer kafkaBusinessMessageProducer() {
        KafkaBusinessMessageProducer kafkaBusinessMessageProducer = new KafkaBusinessMessageProducer();
        log.info("###### 已加载 kafkaBusinessMessageProducer ######");
        return kafkaBusinessMessageProducer;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.rabbitmq", name = "host")
    public RabbitBusinessMessageProducer rabbitBusinessMessageProducer() {
        RabbitBusinessMessageProducer rabbitBusinessMessageProducer = new RabbitBusinessMessageProducer();
        log.info("###### 已加载 rabbitBusinessMessageProducer ######");
        return rabbitBusinessMessageProducer;
    }

}
