package com.example.killserver.consumer;

import com.example.commons.utils.AliJsonUtil;
import com.example.killserver.dao.KillOrderMapper;
import com.example.killserver.model.KillGoods;
import com.example.killserver.model.KillOrder;
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

    @Autowired
    private KillOrderMapper killOrderMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 超时订单死信队列 handler
     */
    @RabbitListener(queues = "timeOutOrderDeadQueue")
    @RabbitHandler
    public void processDead(KillOrder killOrder, Channel channel, Message message) throws IOException {
        try {
            System.out.println("DeadQueue消费者收到消息并ACK返回  : " + killOrder);
            KillOrder dataBaseKillOrder = killOrderMapper.selectById(killOrder.getId());
            //
            if ("new".equals(dataBaseKillOrder.getStatus())) {
                System.out.println("killOrder:" + AliJsonUtil.objectToJsonStr(killOrder));
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
