package com.example.pluginmq.eventBus.publish;

import com.example.pluginmq.eventBus.event.BaseEvent;
import com.example.pluginmq.eventBus.handler.BaseEventBusHandler;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class EventPublish {

    @Autowired
    private List<BaseEventBusHandler> baseEventBusHandlerList;

    private EventBus eventBus;


    /**
     * 注册事件到 eventBus
     */
    @PostConstruct
    private void registerEventBusListenerList() {
        // 多线程版
//        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
//                .setNameFormat("eventBus-pool-%d").build();
//        ExecutorService pool = new ThreadPoolExecutor(2, 4,
//                0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//        eventBus = new AsyncEventBus("test", pool);

        // google eventBus
        eventBus = new EventBus();
        //
        for (BaseEventBusHandler baseEventBusHandler : baseEventBusHandlerList) {
            eventBus.register(baseEventBusHandler);
        }
    }

    /**
     * 发布事件
     */
    public void publishEvent(BaseEvent event) {
        if (event == null) {
            log.error("派发事件不能为null。");
            return;
        }
        //
        eventBus.post(event);
    }

}
