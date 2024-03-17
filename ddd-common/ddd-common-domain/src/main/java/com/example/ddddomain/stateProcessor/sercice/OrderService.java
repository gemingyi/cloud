package com.example.ddddomain.stateProcessor.sercice;

import com.example.ddddomain.stateProcessor.StateContext;
import com.example.ddddomain.stateProcessor.OrderStateMachine;
import com.example.ddddomain.stateProcessor.event.OrderStateEnum;
import com.example.ddddomain.stateProcessor.state.OrderEventEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderStateMachine orderStateMachine;


    public void userPay(String orderId) {
        StateContext context = new StateContext(OrderStateEnum.WAIT_PAY, OrderEventEnum.PAY);
        orderStateMachine.perform(orderId, context);
    }
}
