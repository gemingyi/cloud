package com.example.killserver.mq.consumer;

import com.example.commons.utils.json.FastJsonUtil;
import com.example.killserver.dao.mapper.KillOrderMapper;
import com.example.killserver.dao.entity.KillGoods;
import com.example.killserver.dao.entity.KillOrder;
import com.example.killserver.mq.constans.QueueConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class TimeOutOrderListener {

    private static final String QUEUE = QueueConstant.TIMEOUT_ORDER_DEAD_QUEUE;

    @Autowired
    private KillOrderMapper killOrderMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 超时订单死信队列 handler
     */
    @RabbitListener(queues = QUEUE)
    @RabbitHandler
    public void processDead(KillOrder killOrder, Channel channel, Message message) throws IOException {
        try {
            System.out.println("DeadQueue消费者收到消息并ACK返回  : " + killOrder);
            KillOrder dataBaseKillOrder = killOrderMapper.selectById(killOrder.getId());
            //
            if ("new".equals(dataBaseKillOrder.getStatus())) {
                System.out.println("killOrder:" + FastJsonUtil.objectToJsonStr(killOrder));
                //删除redis中秒杀订单
                redisTemplate.opsForHash().delete("kill:order", killOrder.getOrderNumber());
                //更新用户订单状态(超时关闭)
                KillOrder updateKillOrder = new KillOrder();
                updateKillOrder.setId(killOrder.getId());
                updateKillOrder.setStatus("close");
                killOrderMapper.updateById(updateKillOrder);

                //回退redis中秒杀商品库存
                KillGoods killGoods = (KillGoods) redisTemplate.boundHashOps("kill:goods").get(String.valueOf(killOrder.getKillGoodsId()));
                if (killGoods != null) {
                    killGoods.setCount(killGoods.getCount() + 1);
                    killGoods.setSale(killGoods.getSale() - 1);
                    redisTemplate.boundHashOps("kill:goods").put(String.valueOf(killOrder.getKillGoodsId()), killGoods);
                }
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
