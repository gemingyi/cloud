package com.example.killserver.mq.constans;

/**
 * 队列 常量类
 */
public class QueueConstant {

    /**
     * 超时订单 队列
     */
    public final static String TIMEOUT_ORDER_QUEUE = "timeOutOrderQueue";

    /**
     * 超时订单 死信队列
     */
    public final static String TIMEOUT_ORDER_DEAD_QUEUE = "timeOutOrderDeadQueue";

    /**
     * 死信 队列
     */
    public final static String DEAD_QUEUE = "deadQueue";

}
