package com.example.pluginmq.eventBus.event;

import lombok.Data;


@Data
public class LocalMessageEvent extends BaseEvent {

    /**
     * 0全部 1rabbitMQ 2kafka
     */
    private Integer mqType;

    /**
     *
     */
    private String exchangeOrTopic;

    /**
     *
     */
    private String routingKeyOrPartition;

    /**
     *
     */
    private String messageData;

}
