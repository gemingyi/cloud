package com.example.platformboot.config.threadPool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RefreshScope
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolConfigProperties {

    private List<ThreadPoolConfig> threadPoolConfig;

    public List<ThreadPoolConfig> getThreadPoolConfig() {
        return threadPoolConfig;
    }

    public void setThreadPoolConfig(List<ThreadPoolConfig> threadPoolConfig) {
        this.threadPoolConfig = threadPoolConfig;
    }


    @Getter
    @Setter
    public static class ThreadPoolConfig {

        private String threadPoolName;

        private Integer corePoolSize;

        private Integer queueCapacity;

        private Integer maxPoolSize;

        private Integer keepAliveSeconds;

        private boolean primaryFlag;

    }

}


