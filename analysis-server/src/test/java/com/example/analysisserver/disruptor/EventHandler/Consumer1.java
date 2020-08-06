package com.example.analysisserver.disruptor.EventHandler;

import com.example.analysisserver.disruptor.Event.TestEvent;
import com.example.analysisserver.disruptor.Model.Orange;
import com.lmax.disruptor.EventHandler;

/**
 * 消费者
 *
 * @author Administrator
 * @date 2018/4/15
 */
public class Consumer1 implements EventHandler<TestEvent> {

    @Override
    public void onEvent(TestEvent orangeEvent, long l, boolean b) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Orange orange = orangeEvent.get();
        System.out.println(Thread.currentThread().getName() + "吃了" + orange.toString());
    }
}
