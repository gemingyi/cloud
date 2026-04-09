package com.example.userserver.disruptor.consumer;

import com.example.userserver.disruptor.Orange;
import com.example.userserver.disruptor.TestEvent;
import com.lmax.disruptor.EventHandler;

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