package com.example.killserver.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderDeadConfig {


    /**
     * 超时订单队列
     */
    @Bean
    public Queue timeOutOrderQueue() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", "timeOutOrderDeadExchange");
        params.put("x-dead-letter-routing-key", "timeOutOrderDeadRouting");
        //消息3分钟过期
        params.put("x-message-ttl", 3 * 60 * 1000);
        return new Queue("timeOutOrderQueue",true, false, false, params);
    }

    /**
     * 超时订单交换机
     */
    @Bean
    DirectExchange timeOutOrderExchange() {
        return new DirectExchange("timeOutOrderExchange");
    }

    /**
     * 超时订单队列 绑定 超时订单交换机
     */
    @Bean
    Binding bindingTimeOutOrderQueue() {
        return BindingBuilder.bind(timeOutOrderQueue()).to(timeOutOrderExchange()).with("timeOutOrderRouting");
    }

    /**
     * 超时订单死信队列
     */
    @Bean
    public Queue timeOutOrderDeadQueue() {
        return new Queue("timeOutOrderDeadQueue",true);
    }

    /**
     * 超时订单死信交换机
     */
    @Bean
    DirectExchange timeOutOrderDeadExchange() {
        return new DirectExchange("timeOutOrderDeadExchange");
    }

    /**
     * 超时订单死信队列 绑定 超时订单死信交换机
     */
    @Bean
    Binding bindingTimeOutOrderDeadQueue() {
        return BindingBuilder.bind(timeOutOrderDeadQueue()).to(timeOutOrderDeadExchange()).with("timeOutOrderDeadRouting");
    }

}
