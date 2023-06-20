package com.example.pluginmq.eventBus.handler;

import com.example.pluginmq.dao.mapper.LocalMessageMapper;
import com.example.pluginmq.eventBus.bus.EventRegistry;
import com.example.pluginmq.eventBus.event.LocalMessageEvent;
import com.example.pluginmq.producer.kafka.KafkaBusinessMessageProducer;
import com.example.pluginmq.producer.rabbit.RabbitBusinessMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * https://www.pianshen.com/article/8729285488/ protobuf
 */
@Slf4j
@Component
public class LocalMessageHandler extends BaseEventBusHandler<LocalMessageEvent> {

    @Autowired
    private LocalMessageMapper localMessageMapper;

    private KafkaBusinessMessageProducer kafkaBusinessMessageProducer;
    private RabbitBusinessMessageProducer rabbitBusinessMessageProducer;

    public void setKafkaBusinessMessageProducer(KafkaBusinessMessageProducer kafkaBusinessMessageProducer) {
        this.kafkaBusinessMessageProducer = kafkaBusinessMessageProducer;
    }

    public void setRabbitBusinessMessageProducer(RabbitBusinessMessageProducer rabbitBusinessMessageProducer) {
        this.rabbitBusinessMessageProducer = rabbitBusinessMessageProducer;
    }


    @Override
    public void autoRegister() {
        EventRegistry.getInstance().register(LocalMessageEvent.class, this);
    }

    @Override
    public Integer getPriority() {
        return 1;
    }

    @Override
    public void invoke(LocalMessageEvent event) {
        String messageId = event.getEventId();
        Integer mqType = event.getMqType();
        String exchangeOrTopic = event.getExchangeOrTopic();
        String routingKeyOrPartition = event.getRoutingKeyOrPartition();
        String messageData = event.getMessageData();

        switch (mqType) {
            case 0:
                if (rabbitBusinessMessageProducer != null) {
                    rabbitBusinessMessageProducer.sendBusinessMsg(exchangeOrTopic, routingKeyOrPartition, messageData);
                }
                if (kafkaBusinessMessageProducer != null) {
                    kafkaBusinessMessageProducer.sendBusinessMsg(exchangeOrTopic, Integer.parseInt(routingKeyOrPartition), messageData);
                }
                break;
            case 1:
                if (rabbitBusinessMessageProducer != null) {
                    rabbitBusinessMessageProducer.sendBusinessMsg(exchangeOrTopic, routingKeyOrPartition, messageData);
                }
                break;
            case 2:
                if (kafkaBusinessMessageProducer != null) {
                    kafkaBusinessMessageProducer.sendBusinessMsg(exchangeOrTopic, Integer.parseInt(routingKeyOrPartition), messageData);
                }
                break;
            default:
                log.error("MQ类型缺失不发送消息[event:{}]", event);
        }
    }

}
