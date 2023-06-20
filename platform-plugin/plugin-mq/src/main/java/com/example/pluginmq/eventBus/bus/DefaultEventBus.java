package com.example.pluginmq.eventBus.bus;

import com.example.pluginmq.eventBus.event.BaseEvent;
import com.example.pluginmq.eventBus.handler.BaseEventBusHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class DefaultEventBus {

    /**
     * 发布 event事件
     */
    public static void publish(BaseEvent event) {
        if (event == null) {
            log.error("派发事件不能为null。");
            return;
        }

        //
        List<BaseEventBusHandler> handlers = EventRegistry.getInstance().findRegister(event.getClass());
        if (CollectionUtils.isEmpty(handlers)) {
            return;
        }

        //
        handlers.stream().sorted(Comparator.comparing(BaseEventBusHandler::getPriority))
                .forEach(handler -> handler.handler(event));
    }

}
