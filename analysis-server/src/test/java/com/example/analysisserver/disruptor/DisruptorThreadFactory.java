package com.example.analysisserver.disruptor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DisruptorThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;

    DisruptorThreadFactory() {
        this.namePrefix = "disruptor-" +
                poolNumber.getAndIncrement() +
                "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
        if (t.isDaemon()) {
            t.setDaemon(true);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}