package com.example.pluginmq.eventBus.event;

import com.example.commons.utils.IdWorker;
import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseEvent {

    private IdWorker idWorker = new IdWorker(null);

    public BaseEvent() {
        this.eventId = String.valueOf(idWorker.nextId());
        this.publishDate = new Date();
    }

    private String eventId;

    /**
     * event name
     */
    private String eventName;

    /**
     * 发布时间
     */
    private Date publishDate;

}
