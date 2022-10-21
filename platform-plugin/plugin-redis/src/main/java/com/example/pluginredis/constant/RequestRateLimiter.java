package com.example.pluginredis.constant;

import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/29 11:39
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestRateLimiter {

    /**
     * 限流的key
     */
    String key();

    /**
     * 限流模式,默认单机
     */
    RateType type() default RateType.PER_CLIENT;

    /**
     * 限流速率，默认每秒1
     */
    @Value("")
    long rate() default 1;

    /**
     * 限流速率
     */
    long rateInterval() default 500;

    /**
     * 限流速率单位
     */
    RateIntervalUnit timeUnit() default RateIntervalUnit.MILLISECONDS;
}
