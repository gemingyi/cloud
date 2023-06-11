package com.example.pluginmq.eventBus.bus;

import com.example.pluginmq.eventBus.event.BaseEvent;
import com.example.pluginmq.eventBus.handler.BaseEventBusHandler;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class EventRegistry {

    private EventRegistry() {}


    private volatile static EventRegistry INSTANCE;

    public static EventRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (EventRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EventRegistry();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 监听器映射map key:eventClass Name value:handlerList
     */
    private Map<String, List<BaseEventBusHandler>> registerListenerMap = Maps.newConcurrentMap();

    /**
     * 获取事件全部 事件映射关系
     */
    public Map<String, List<BaseEventBusHandler>> getRegisterListenerMap() {
        return registerListenerMap;
    }

    /**
     * 注册 监听器
     */
    public void register(Class<? extends BaseEvent> baseEvent, BaseEventBusHandler handler) {
        if (baseEvent == null) {
            return;
        }

        registerListenerMap.computeIfAbsent(baseEvent.getName(), k -> new ArrayList<>());
        registerListenerMap.get(baseEvent.getName()).add(handler);
    }

    /**
     * 查找 监听器
     */
    public List<BaseEventBusHandler> findRegister(Class<? extends BaseEvent> baseEvent) {
        if (baseEvent == null) {
            return null;
        }
        return registerListenerMap.get(baseEvent.getName());
    }

}
