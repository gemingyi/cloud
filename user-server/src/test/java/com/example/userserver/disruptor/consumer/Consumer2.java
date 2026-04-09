package com.example.userserver.disruptor.consumer;

import com.example.userserver.disruptor.Orange;
import com.example.userserver.disruptor.TestEvent;
import com.lmax.disruptor.EventHandler;

public class Consumer2 implements EventHandler<TestEvent> {

    @Override
    public void onEvent(TestEvent orangeEvent, long l, boolean b) throws Exception {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Orange orange = orangeEvent.get();
        System.out.println(Thread.currentThread().getName() + "清理了消费者1、消费者2吃剩下" + orange.toString() + "的橘子皮");
    }
}
