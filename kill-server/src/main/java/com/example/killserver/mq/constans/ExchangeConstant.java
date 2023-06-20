package com.example.killserver.mq.constans;

/**
 * mq 交换机产量类
 */
public class ExchangeConstant {

    /**
     * 超时订单 交换机
     */
    public final static String TIMEOUT_ORDER_EXCHANGE = "timeOutOrderExchange";

    /**
     * 超时订单 死信交换机
     */
    public final static String TIMEOUT_ORDER_DEAD_EXCHANGE = "timeOutOrderDeadExchange";

    /**
     * 死信 交换机
     */
    public final static String DEAD_EXCHANGE = "deadExchange";

}
