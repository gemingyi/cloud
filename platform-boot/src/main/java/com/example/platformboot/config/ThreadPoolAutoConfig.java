package com.example.platformboot.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@EnableConfigurationProperties
public class ThreadPoolAutoConfig implements ImportBeanDefinitionRegistrar, EnvironmentAware, PriorityOrdered {

    private Logger log = LoggerFactory.getLogger(ThreadPoolAutoConfig.class);

    public ThreadPoolAutoConfig() {
    }

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Binder binder = Binder.get(this.environment);
        Map<String, ThreadPoolProperties> map = binder.bind("spring.ttl.threadpool",
                Bindable.mapOf(String.class, ThreadPoolProperties.class)).orElseGet(HashMap::new);
        map.forEach((name, threadPoolProperties) -> {
            BeanDefinition beanDefinition = new RootBeanDefinition(Executor.class, () -> {
                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
                executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
                executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
                executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
                executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
                executor.setThreadNamePrefix(name + "-ttl-executor-");
                executor.setRejectedExecutionHandler((r, executor1) -> {
                    log.error("task Reject!");
                });
                log.info("====mainExecutor=====coreSize: {}, queueSize:{}, maxSize:{}",
                        threadPoolProperties.getCorePoolSize(), threadPoolProperties.getQueueCapacity(), threadPoolProperties.getMaxPoolSize());
                executor.initialize();
                ExecutorService ex = executor.getThreadPoolExecutor();
                return TtlExecutors.getTtlExecutor(ex);
            });
            beanDefinition.setPrimary(threadPoolProperties.getPrimaryFlag());
            registry.registerBeanDefinition(name + "TtlExecutor", beanDefinition);
        });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}