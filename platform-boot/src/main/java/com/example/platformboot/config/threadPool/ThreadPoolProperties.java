package com.example.platformboot.config.threadPool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author mingyi ge
 * @Description: 线程池 配置类
 * @date 2020/12/14 14:01
 */
@Component
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolProperties {

    private Integer corePoolSize;

    private Integer queueCapacity;

    private Integer maxPoolSize;

    private Integer keepAliveSeconds;

    private Boolean primaryFlag = false;

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(Integer keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public Boolean getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(Boolean primaryFlag) {
        this.primaryFlag = primaryFlag;
    }
}
