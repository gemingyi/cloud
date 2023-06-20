package com.example.killserver.controller;

import com.example.commons.result.RestResult;
import com.example.killserver.dao.entity.KillGoods;
import com.example.killserver.dao.entity.KillOrder;
import com.example.killserver.service.IKillGoodsService;
import com.example.killserver.service.IKillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping(value = "kill")
public class KillController {

    @Autowired
    private IKillGoodsService killGoodsServiceImpl;
    @Autowired
    private IKillOrderService killOrderServiceImpl;

    CountDownLatch countDownLatch = new CountDownLatch(5000);
    ExecutorService executorService = Executors.newFixedThreadPool(12);

    /**
     * 添加秒杀商品
     */
    @GetMapping(value = "create")
    public void CreateGoods() {
        KillGoods killGoods = new KillGoods();
        killGoods.setGoodsId(37);
        killGoods.setTitle("测试商品1");
        killGoods.setPrice(new BigDecimal("200.00"));
        killGoods.setCostPrice(new BigDecimal("100.00"));
        killGoods.setCount(5);
        killGoods.setSale(0);
        killGoods.setVersion(0);
        killGoodsServiceImpl.addKillGoods(killGoods);
    }

    @GetMapping(value = "getKillGoodsDetail")
    public void getKillGoodsDetail() {
        Integer goodsId = 37;
        KillGoods killGoods = killGoodsServiceImpl.getKillGoods(goodsId);
        System.out.println(killGoods);
    }

    /**
     * 模拟多用户提交订单
     */
    @GetMapping(value = "submit")
    public void submitOrder() {
        for (int i = 1; i <= 100; i++) {
            final int userId = i;
            Runnable task = () -> {
                RestResult<Object> result = killOrderServiceImpl.submitOrder(37, userId, 1103);
                System.out.println(result.getMessage());
                countDownLatch.countDown();
            };
            executorService.execute(task);
        }
        try {
            countDownLatch.await();
            KillGoods killGoods = killGoodsServiceImpl.getKillGoods(37);
            System.out.println(killGoods);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的订单
     */
    @GetMapping(value = "myOrder")
    public void listMyOrder() {
        Integer userId = 67;
        List<KillOrder> killOrderList = killOrderServiceImpl.findKillOrderByUser(userId);
        System.out.println(killOrderList);
    }

    /**
     * 订单详情
     */
    @GetMapping(value = "getOrderDetail")
    public void getOrderDetail() {
        String orderNumber = "1582267370019";
        KillOrder killOrder = killOrderServiceImpl.getOrderByNumber(orderNumber);
        System.out.println(killOrder);
    }

    /**
     * 模拟订单超时回退库存(定时任务处理、延时队列)
     */
    @GetMapping(value = "overTime")
    public void overTimeOrder() {
        String orderNumber = "1582267370019";
        killOrderServiceImpl.overTimeCloseOrder(orderNumber);
    }

    /**
     * 模拟正常政支付
     */
    @GetMapping(value = "paid")
    public void paid() {
        String orderNumber = "1582267370000";
        killOrderServiceImpl.PaidSaveOrder(orderNumber);
    }

}
