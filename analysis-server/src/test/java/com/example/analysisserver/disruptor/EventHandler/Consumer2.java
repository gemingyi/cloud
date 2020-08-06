package com.example.analysisserver.disruptor.EventHandler;

import com.example.analysisserver.disruptor.Model.Orange;
import com.example.analysisserver.disruptor.Event.TestEvent;
import com.lmax.disruptor.EventHandler;

/**
 * 消费者
 * Created by Administrator on 2018/6/3.
 */
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
