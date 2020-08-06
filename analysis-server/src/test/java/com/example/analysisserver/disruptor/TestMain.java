package com.example.analysisserver.disruptor;

import com.example.analysisserver.disruptor.Event.TestEvent;
import com.example.analysisserver.disruptor.EventHandler.Consumer;
import com.example.analysisserver.disruptor.EventHandler.Consumer1;
import com.example.analysisserver.disruptor.EventHandler.Consumer2;
import com.example.analysisserver.disruptor.Model.Orange;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;



/**
 * Created by Administrator on 2018/4/15.
 */
public class TestMain {

    private static final int BUFFER_SIZE = 1;
    private static final EventFactory<TestEvent> EVENT_FACTORY = new EventFactory<TestEvent>() {
        @Override
        public TestEvent newInstance() {
            return new TestEvent();
        }
    };
    private static Disruptor<TestEvent> disruptor;


    public static void main(String[] args) {
        //
        init();
        //消费者一，这里可以并行多个
        handleEventsWith(new Consumer(), new Consumer1(), new Consumer2());
        //
        start();
        //
        EventPublisher eventPublisher = new EventPublisher(disruptor.getRingBuffer());
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Orange orange = new Orange(i, "橘子");
            eventPublisher.publish(orange);
        }
        //
        shutdown();
    }

    private static void init() {
        disruptor = new Disruptor<TestEvent>(EVENT_FACTORY, BUFFER_SIZE, new DisruptorThreadFactory(), ProducerType.SINGLE, new SleepingWaitStrategy());
    }

    private static void handleEventsWith(Consumer consumer, Consumer1 consumer1, Consumer2 consumer2) {
//        EventHandlerGroup<TestEvent> handlerGroup = disruptor.handleEventsWith(consumer, consumer1, consumer, consumer1);
        EventHandlerGroup<TestEvent> handlerGroup = disruptor.handleEventsWith(consumer, consumer1);
        handlerGroup.then(consumer2);
    }

    private static void start() {
        disruptor.start();
    }

    private static void shutdown() {
        disruptor.shutdown();
    }

}
