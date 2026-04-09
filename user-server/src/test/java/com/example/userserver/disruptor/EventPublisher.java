package com.example.userserver.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class EventPublisher {

    private final EventTranslatorOneArg<TestEvent, Orange> producer = new EventTranslatorOneArg<TestEvent, Orange>() {
        @Override
        public void translateTo(TestEvent testEvent, long l, Orange orange) {
            testEvent.set(orange);
        }
    };


    private RingBuffer<TestEvent> ringBuffer;

    public EventPublisher(RingBuffer<TestEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void publish(Orange orange) {
        ringBuffer.publishEvent(producer, orange);
        System.out.println(Thread.currentThread().getName() + "生产了" + orange.toString());
    }

}
