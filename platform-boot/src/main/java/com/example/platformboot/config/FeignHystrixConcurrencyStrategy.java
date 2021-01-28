//package com.example.platformboot.config;
//
//import com.netflix.hystrix.HystrixThreadPoolKey;
//import com.netflix.hystrix.HystrixThreadPoolProperties;
//import com.netflix.hystrix.strategy.HystrixPlugins;
//import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
//import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
//import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
//import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
//import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
//import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
//import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
//import com.netflix.hystrix.strategy.properties.HystrixProperty;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
//* @Description feign传递请求头问题
//* https://www.jianshu.com/p/f30892335057
//* @Author mingyi ge
//* @Date 2020/10/15 17:00
//*/
//@Slf4j
//@Component
////gray 里面已经有一个配置了
//@ConditionalOnProperty(prefix = "loverent.gray", value = "service-open", havingValue = "false")
//public class FeignHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {
//
//    //Hystrix并发策略
//    private HystrixConcurrencyStrategy delegate;
//
//    public FeignHystrixConcurrencyStrategy() {
//        try {
//            this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();
////            if (this.delegate instanceof SleuthHystrixConcurrencyStrategy) {
////                // Welcome to singleton hell...
////                return;
////            }
//            HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins
//                    .getInstance().getCommandExecutionHook();
//            HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance()
//                    .getEventNotifier();
//            HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance()
//                    .getMetricsPublisher();
//            HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance()
//                    .getPropertiesStrategy();
//            HystrixPlugins.reset();
//            HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
//            HystrixPlugins.getInstance()
//                    .registerCommandExecutionHook(commandExecutionHook);
//            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
//            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
//            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
//        } catch (Exception e) {
//            log.error("Failed to register Sleuth Hystrix Concurrency Strategy", e);
//        }
//    }
//
//    @Override
//    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
//                                            HystrixProperty<Integer> corePoolSize,
//                                            HystrixProperty<Integer> maximumPoolSize,
//                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
//                                            BlockingQueue<Runnable> workQueue) {
//        return this.delegate.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
//                keepAliveTime, unit, workQueue);
//    }
//
//    @Override
//    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
//                                            HystrixThreadPoolProperties threadPoolProperties) {
//        return this.delegate.getThreadPool(threadPoolKey, threadPoolProperties);
//    }
//
//    @Override
//    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
//        return this.delegate.getBlockingQueue(maxQueueSize);
//    }
//
//    @Override
//    public <T> HystrixRequestVariable<T> getRequestVariable(
//            HystrixRequestVariableLifecycle<T> rv) {
//        return this.delegate.getRequestVariable(rv);
//    }
//
//
//    @Override
//    public <T> Callable<T> wrapCallable(Callable<T> callable) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        //Hystrix 新线程去处理
//        return new WrappedCallable<>(callable, requestAttributes);
//    }
//
//    static class WrappedCallable<T> implements Callable<T> {
//
//        private final Callable<T> target;
//        private final RequestAttributes requestAttributes;
//
//        public WrappedCallable(Callable<T> target, RequestAttributes requestAttributes) {
//            this.target = target;
//            this.requestAttributes = requestAttributes;
//        }
//
//        @Override
//        public T call() throws Exception {
//            try {
//                RequestContextHolder.setRequestAttributes(requestAttributes);
//                return target.call();
//            } finally {
//                RequestContextHolder.resetRequestAttributes();
//            }
//        }
//    }
//}
