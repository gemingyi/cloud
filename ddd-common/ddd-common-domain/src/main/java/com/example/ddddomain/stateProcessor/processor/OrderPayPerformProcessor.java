package com.example.ddddomain.stateProcessor.processor;

import com.example.ddddomain.stateProcessor.StateContext;
import com.example.ddddomain.stateProcessor.event.OrderStateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderPayPerformProcessor implements IPerformProcessor<String, StateContext> {

    @Override
    public boolean validate(String param, StateContext context) {
        String orderId = param;
        Integer dataBaseOrderState = OrderStateEnum.WAIT_PAY.getState();
        if (!dataBaseOrderState.equals(context.getSource().getState())) {
            log.info("校验订单状态orderId:{}, source状态:{}, target状态:{}", param, dataBaseOrderState, context.getTarget());
            return false;
        }
        return true;
    }

    @Override
    public void action(String param, StateContext context) {
        // 更新订单状态
        log.info("更新订单orderId:{}, 状态:{}", param, context.getTarget());
    }

}
