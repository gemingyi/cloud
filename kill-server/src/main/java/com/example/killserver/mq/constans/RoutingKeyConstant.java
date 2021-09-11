package com.example.killserver.mq.constans;

/**
 * 路由键 常量类
 */
public class RoutingKeyConstant {

    /**
     * 超时订单 路由键
     */
    public final static String TIMEOUT_ORDER_KEY = "timeOutOrderRouting";

    /**
     * 超时订单 路由键
     */
    public final static String TIMEOUT_ORDER_DEAD_KEY = "timeOutOrderDeadRouting";


    /**
     * 死信 路由键
     */
    public final static String DEAD_ROUTING_KEY = "deadRoutingKey";
}
