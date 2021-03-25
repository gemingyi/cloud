package com.example.killserver;

import com.example.killserver.model.KillOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KillServerApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
        rabbitTemplate.convertAndSend("timeOutOrderExchange", "timeOutOrderRouting", killOrder, message -> {
            return message;
        });
    }

}
