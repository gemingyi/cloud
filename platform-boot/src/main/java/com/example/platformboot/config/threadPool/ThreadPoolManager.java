package com.example.platformboot.config.threadPool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * https://www.cnblogs.com/crazymakercircle/p/17398803.html#autoid-h2-5-0-0
 */
@Slf4j
@Configuration
public class ThreadPoolManager {

    @Autowired
    private ThreadPoolConfigProperties threadPoolConfigProperties;

    @Autowired
    private Environment environment;

    /**
     * 存储线程池对象
     */
    public Map<String, MonitorThreadPool> threadPoolExecutorMap = new HashMap<>();

    /**
     * 根据配置信息，初始化线程池
     */
    @PostConstruct
    public void init() {
        createThreadPools(threadPoolConfigProperties);
    }

    /**
     * 初始化线程池的创建
     *
     * @param threadPoolConfigProperties
     */
    private void createThreadPools(ThreadPoolConfigProperties threadPoolConfigProperties) {
//        Binder binder = Binder.get(this.environment);
//        Map<String, ThreadPoolProperties> map = binder.bind("thread.pool",
//                Bindable.mapOf(String.class, ThreadPoolProperties.class)).orElseGet(HashMap::new);
//        map.forEach((name, threadPoolProperties) -> {
//            if (!threadPoolExecutorMap.containsKey(name)) {
//                MonitorThreadPool threadPoolMonitor = new MonitorThreadPool(
//                        threadPoolProperties.getCorePoolSize(),
//                        threadPoolProperties.getMaxPoolSize(),
//                        threadPoolProperties.getKeepAliveSeconds(),
//                        TimeUnit.SECONDS,
//                        new LinkedBlockingQueue<>(threadPoolProperties.getQueueCapacity()),
//                        name
//                );
//                threadPoolExecutorMap.put(name, threadPoolMonitor);
//            }
//        });

        if (CollectionUtils.isNotEmpty(threadPoolConfigProperties.getThreadPoolConfig())) {
            threadPoolConfigProperties.getThreadPoolConfig().forEach(threadPoolConfig -> {
                if (!threadPoolExecutorMap.containsKey(threadPoolConfig.getThreadPoolName())) {
                    MonitorThreadPool threadPoolMonitor = new MonitorThreadPool(
                            threadPoolConfig.getCorePoolSize(),
                            threadPoolConfig.getMaxPoolSize(),
                            threadPoolConfig.getKeepAliveSeconds(),
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(threadPoolConfig.getQueueCapacity()),
                            threadPoolConfig.getThreadPoolName()
                    );
                    threadPoolExecutorMap.put(threadPoolConfig.getThreadPoolName(), threadPoolMonitor);
                }
            });
        }
    }

    // 根据线程池名称，获取对应线程池实例
    public MonitorThreadPool getThreadPoolExecutor(String poolName) {
        MonitorThreadPool threadPoolExecutorForMonitor = threadPoolExecutorMap.get(poolName);
        if (threadPoolExecutorForMonitor == null) {
            throw new RuntimeException("找不到名字为" + poolName + "的线程池");
        }
        return threadPoolExecutorForMonitor;
    }

    // 获取线程池Map
    public Map<String, MonitorThreadPool> getThreadPoolExecutorMap() {
        return threadPoolExecutorMap;
    }


    /**
     * 监听事件
     */
    @EventListener
    public void envListener(EnvironmentChangeEvent event) {
        log.info("配置发生变更" + event);
        changeThreadPools(threadPoolConfigProperties);
    }

    /**
     * 调整线程池
     *
     * @param threadPoolConfigProperties
     */
    private void changeThreadPools(ThreadPoolConfigProperties threadPoolConfigProperties) {
        if (CollectionUtils.isNotEmpty(threadPoolConfigProperties.getThreadPoolConfig())) {
            threadPoolConfigProperties.getThreadPoolConfig().forEach(config -> {
                ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(config.getThreadPoolName());
                if (Objects.nonNull(threadPoolExecutor)) {
                    threadPoolExecutor.setCorePoolSize(config.getCorePoolSize());
                    threadPoolExecutor.setMaximumPoolSize(config.getMaxPoolSize());
                    threadPoolExecutor.setKeepAliveTime(config.getKeepAliveSeconds(), TimeUnit.SECONDS);
                }
            });
        }
    }

}
