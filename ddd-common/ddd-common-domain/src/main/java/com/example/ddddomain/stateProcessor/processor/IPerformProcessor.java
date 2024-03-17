package com.example.ddddomain.stateProcessor.processor;

public interface IPerformProcessor<R, StateContext> {

    /**
     * 校验
     * @param param 业务入参 如订单信息
     * @param context   上下文
     * @return
     */
    boolean validate(R param, StateContext context);

    /**
     * 执行业务
     * @param param 业务入参 如订单信息
     * @param context   上下文
     */
    void action(R param, StateContext context);

}
