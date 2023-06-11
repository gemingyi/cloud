package com.example.pluginmq.eventBus.handler;

import com.example.pluginmq.eventBus.event.BaseEvent;
import com.google.common.base.Stopwatch;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 事件处理基类
 */
@Slf4j
public abstract class BaseEventBusHandler<E extends BaseEvent> {

    /**
     * 注册 handler
     */
    @PostConstruct
    public abstract void autoRegister();

    /**
     * handler 优先级
     */
    public abstract Integer getPriority();


    /**
     * 下面2个注解    是google的 eventBus  用这个类 EventPublish 发布事件
     */
    @Subscribe
    @AllowConcurrentEvents
    public final void handler(E event) {
        String eventName = event.getEventName();
        Stopwatch start = Stopwatch.createStarted();
        try {
            log.info("开始处理事件 [eventName:{}], [event:{}]", eventName, event);
            this.invoke(event);
        } catch (Exception e) {
            log.error("处理事件异常 [eventName:{}], [event:{}], [e:{}]" , eventName, event, e);
        } finally {
            log.info("结束处理事件 [eventName:{}], [event:{}], [耗时:{}]", eventName, event, start.stop().elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public abstract void invoke(E baseEvent);
}
