package com.example.ddddomain.stateProcessor;

import com.example.ddddomain.stateProcessor.event.OrderStateEnum;
import com.example.ddddomain.stateProcessor.state.OrderEventEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateContext {

    private OrderStateEnum source;

    private OrderEventEnum onEvent;

    private OrderStateEnum target;


    public StateContext(OrderStateEnum source, OrderEventEnum onEvent) {
        this.setSource(source);
        this.setOnEvent(onEvent);
    }
}
