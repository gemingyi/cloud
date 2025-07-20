package com.example.platformboot.config.threadPool;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

//@Configuration
//@Import(ThreadPoolConfig.class)
public class ThreadPoolAutoConfig {

}

class ThreadPoolConfig implements ImportBeanDefinitionRegistrar, EnvironmentAware, PriorityOrdered {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Binder binder = Binder.get(this.environment);
        Map<String, ThreadPoolProperties> map = binder.bind("thread.pool",
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
                });
                executor.initialize();
                ExecutorService ex = executor.getThreadPoolExecutor();
                return TtlExecutors.getTtlExecutor(ex);
            });
            beanDefinition.setPrimary(Boolean.TRUE.equals(threadPoolProperties.getPrimaryFlag()));
            String beanName = StringUtils.uncapitalize(name) + "TtlExecutor";
            registry.registerBeanDefinition(beanName, beanDefinition);
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}