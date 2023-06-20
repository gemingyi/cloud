package com.example.killserver.mq.consumer;

import com.example.killserver.mq.constans.QueueConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @description:    死信队列消费者
 * @author: mingyi ge
 * @date: 2021/6/23 16:11
 */
@Component
@Slf4j
public class DeadLetterConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String QUEUE = QueueConstant.DEAD_QUEUE;


    /**
     * 三方服务 死信队列
     */
    @SuppressWarnings("unchecked")
    @RabbitListener(queues = QUEUE)
    public void redeliverMessage(Message failedMessage, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        log.info("<<<---+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+---->>> 死信队列消费者 开始处理死信消息！");
        log.info("<<<---+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+---->>> 死信队列消费者 接收到死信消息:[{}]", failedMessage.toString());
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        byte[] bytes = failedMessage.getBody();
        String message = new String(bytes, StandardCharsets.UTF_8);
        log.info("<<<---+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+---->>> 死信队列消费者 消息内容:[{}]", message);
        Map<String, Object> proHeaders = failedMessage.getMessageProperties().getHeaders();
        List<Map<String, Object>> xDeath = (List<Map<String, Object>>) proHeaders.get("x-death");
        Map<String, Object> map = xDeath.get(0);
        String exchange = (String) map.get("exchange");
        List<String> routingKeys = (List<String>) map.get("routing-keys");
        long count = (long) map.get("count");
        // 重试次数小于5次发送到原队列
        if (count <= 5) {
            log.info("<<<---+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+---->>> 死信队列消费者 消息重试次数:[{}]小于5次发送到原队列！", count);
            long delay = 6000 * count;
            // 第三次重试时间间隔修改为10分钟
            if (count > 3) {
                delay = 6000 * 10 * count;
            }
            failedMessage.getMessageProperties().getHeaders().put("x-delay", delay);
            this.rabbitTemplate.send(exchange, routingKeys.get(0), failedMessage);
            channel.basicAck(tag, false);
        } else {
            log.info("<<<---+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+---->>> 三方服务死信队列消费者 消息重试次数超过5次发送到三方服务暂存队列！");
            log.info("{}", failedMessage);
//            this.rabbitTemplate.send(QueueEnum.THIRDPARTY_PARKING_LOT_QUEUE.getName(), failedMessage);
            channel.basicAck(tag, false);
        }
    }

}
