package com.example.killserver.mq.config;

import com.example.killserver.mq.constans.ExchangeConstant;
import com.example.killserver.mq.constans.QueueConstant;
import com.example.killserver.mq.constans.RoutingKeyConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KillOrderDeadConfig {


    /**
     * 超时订单队列
     */
    @Bean
    public Queue timeOutOrderQueue() {
        // 绑定死信交换机
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", ExchangeConstant.TIMEOUT_ORDER_DEAD_EXCHANGE);
        params.put("x-dead-letter-routing-key", RoutingKeyConstant.TIMEOUT_ORDER_DEAD_KEY);
        //消息3分钟过期
        params.put("x-message-ttl", 3 * 60 * 1000);
        return new Queue(QueueConstant.TIMEOUT_ORDER_QUEUE,true, false, false, params);
    }

    /**
     * 超时订单交换机
     */
    @Bean
    DirectExchange timeOutOrderExchange() {
        return new DirectExchange(ExchangeConstant.TIMEOUT_ORDER_EXCHANGE);
    }

    /**
     * 超时订单队列 绑定 超时订单交换机
     */
    @Bean
    Binding bindingTimeOutOrderQueue() {
        return BindingBuilder.bind(timeOutOrderQueue()).to(timeOutOrderExchange()).with(RoutingKeyConstant.TIMEOUT_ORDER_KEY);
    }


    /**
     * 死信队列
     */
    @Bean
    public Queue timeOutOrderDeadQueue() {
        return new Queue(QueueConstant.TIMEOUT_ORDER_DEAD_QUEUE,true);
    }

    /**
     * 死信交换机
     */
    @Bean
    DirectExchange timeOutOrderDeadExchange() {
        return new DirectExchange(ExchangeConstant.TIMEOUT_ORDER_DEAD_EXCHANGE);
    }

    /**
     * 死信队列 绑定 死信交换机
     */
    @Bean
    Binding bindingTimeOutOrderDeadQueue() {
        return BindingBuilder.bind(timeOutOrderDeadQueue()).to(timeOutOrderDeadExchange()).with(RoutingKeyConstant.TIMEOUT_ORDER_DEAD_KEY);
    }



    /**
     * 死信队列
     */
    @Bean
    public Queue deadQueue() {
        return new Queue(QueueConstant.DEAD_QUEUE,true);
    }

    /**
     * 死信交换机
     */
    @Bean
    DirectExchange deadExchange() {
        return new DirectExchange(ExchangeConstant.DEAD_EXCHANGE);
    }

    /**
     * 死信队列 绑定 死信交换机
     */
    @Bean
    Binding bindingDeadQueueToDeadExchange() {
        return BindingBuilder.bind(timeOutOrderDeadQueue()).to(timeOutOrderDeadExchange()).with(RoutingKeyConstant.DEAD_ROUTING_KEY);
    }

}
