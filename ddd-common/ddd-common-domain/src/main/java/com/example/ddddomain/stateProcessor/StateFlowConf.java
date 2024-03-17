package com.example.ddddomain.stateProcessor;


import com.example.ddddomain.stateProcessor.event.OrderStateEnum;
import com.example.ddddomain.stateProcessor.processor.IPerformProcessor;
import com.example.ddddomain.stateProcessor.state.OrderEventEnum;
import org.springframework.stereotype.Component;

@Component
public class StateFlowConf {

    public void definePerform(OrderStateEnum source, OrderEventEnum onEvent,
                              OrderStateEnum target, IPerformProcessor<String, StateContext> performProcessor) {
        this.source = source;
        this.onEvent = onEvent;
        this.target = target;
        this.performProcessor = performProcessor;
    }

    private OrderStateEnum source;

    private OrderEventEnum onEvent;

    private OrderStateEnum target;

    private IPerformProcessor<String, StateContext> performProcessor;


    public OrderStateEnum getSource() {
        return source;
    }

    public OrderEventEnum getOnEvent() {
        return onEvent;
    }

    public OrderStateEnum getTarget() {
        return target;
    }

    public IPerformProcessor getPerformProcessor() {
        return performProcessor;
    }
}
