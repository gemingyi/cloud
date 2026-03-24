package com.example.commons.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Function;

public class AsyncUtil {

    private static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(
            1,
            (new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("failAfter-%d").build()
    );

    public AsyncUtil() {
    }

    private static <T> CompletableFuture<T> failAfter(Duration duration) {
        CompletableFuture<T> future = new CompletableFuture();
        SCHEDULER.schedule(() -> {
            return future.completeExceptionally(new TimeoutException());
        }, duration.toMillis(), TimeUnit.MILLISECONDS);
        return future;
    }

    public static <T> CompletableFuture<T> within(CompletableFuture<T> future, Duration duration) {
        CompletableFuture<T> timeoutFuture = failAfter(duration);
        return future.applyToEither(timeoutFuture, Function.identity());
    }

}