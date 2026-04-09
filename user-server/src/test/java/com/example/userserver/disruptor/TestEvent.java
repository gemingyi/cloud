package com.example.userserver.disruptor;

public class TestEvent {

    private Orange orange;

    public void set(Orange orange) {
        this.orange = orange;
    }

    public Orange get() {
        return orange;
    }

}
