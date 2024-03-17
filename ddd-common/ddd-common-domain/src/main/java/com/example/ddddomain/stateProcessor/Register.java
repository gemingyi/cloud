package com.example.ddddomain.stateProcessor;

import com.example.ddddomain.stateProcessor.event.OrderStateEnum;
import com.example.ddddomain.stateProcessor.processor.OrderPayPerformProcessor;
import com.example.ddddomain.stateProcessor.state.OrderEventEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Configuration
public class Register {

    @Autowired
    private OrderPayPerformProcessor orderPayPerformProcessor;

    @Bean
    OrderStateMachine initOrderStateMachine() {
        StateFlowConf stateFlowConf = new StateFlowConf();
        stateFlowConf.definePerform(OrderStateEnum.WAIT_PAY, OrderEventEnum.PAY, OrderStateEnum.PAYED, orderPayPerformProcessor);

        OrderStateMachine orderStateMachine = new OrderStateMachine();
        orderStateMachine.setName("订单状态流转");
        orderStateMachine.setStateFlowConfList(Collections.singletonList(stateFlowConf));
        return orderStateMachine;
    }
}
