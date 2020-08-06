package com.example.analysisserver.disruptor.Event;

import com.example.analysisserver.disruptor.Model.Orange;

/**
 * 生产的对象
 * Created by Administrator on 2018/4/15.
 */
public class TestEvent{

    private Orange orange;

    public void set(Orange orange) {
        this.orange = orange;
    }

    public Orange get() {
        return orange;
    }
}
