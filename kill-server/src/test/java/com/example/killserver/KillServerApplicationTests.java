package com.example.killserver;

import com.example.killserver.dao.entity.KillOrder;
import com.example.killserver.mq.constans.ExchangeConstant;
import com.example.killserver.mq.constans.RoutingKeyConstant;
import com.example.pluginmq.producer.rabbit.RabbitBusinessMessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KillServerApplicationTests {

//    @Autowired
//    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitBusinessMessageProducer rabbitBusinessMessageProducer;

    @Test
    public void contextLoads() {
        KillOrder killOrder = new KillOrder();
        killOrder.setUserId(1);
        killOrder.setKillGoodsId(1);
        killOrder.setBusinessId(1);
        killOrder.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        killOrder.setOrderMoney(BigDecimal.valueOf(100.00));
        killOrder.setStatus("new");
        killOrder.setAdder("测试收货地址111");
        killOrder.setCreateTime(new Date());
        //
        rabbitBusinessMessageProducer.sendBusinessMsg(ExchangeConstant.TIMEOUT_ORDER_EXCHANGE, RoutingKeyConstant.TIMEOUT_ORDER_KEY, killOrder);
    }

}
