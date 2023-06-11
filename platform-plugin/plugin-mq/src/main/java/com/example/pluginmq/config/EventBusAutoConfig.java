package com.example.pluginmq.config;

import com.example.pluginmq.eventBus.handler.LocalMessageHandler;
import com.example.pluginmq.eventBus.publish.EventPublish;
import com.example.pluginmq.producer.kafka.KafkaBusinessMessageProducer;
import com.example.pluginmq.producer.rabbit.RabbitBusinessMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AutoConfigureBefore(MQAutoConfig.class)
public class EventBusAutoConfig {


    @Bean
    public LocalMessageHandler localMessageListener(
            KafkaBusinessMessageProducer kafkaBusinessMessageProducer,
            RabbitBusinessMessageProducer rabbitBusinessMessageProducer) {
        LocalMessageHandler localMessageListener = new LocalMessageHandler();
        localMessageListener.setKafkaBusinessMessageProducer(kafkaBusinessMessageProducer);
        localMessageListener.setRabbitBusinessMessageProducer(rabbitBusinessMessageProducer);
        return localMessageListener;
    }

    @Bean
    public EventPublish eventPublish() {
        EventPublish eventPublish = new EventPublish();
        log.info("###### 已加载 eventPublish ######");
        return eventPublish;
    }

}
