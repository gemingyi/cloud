package com.example.pluginredis.javascript;

import com.example.pluginredis.constant.RequestRateLimiter;
import com.example.pluginredis.util.RedisLock;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateLimiterConfig;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * https://www.cnblogs.com/buguge/p/13256485.html
 * https://blog.csdn.net/weixin_43303530/article/details/123130175
 */
@Slf4j
@Component
public class JavascriptTemplate {

    @Autowired
    private RedisLock redisLock;
    @Autowired
    private RedissonClient redissonClient;



    /**
     * @Description 防止重复提交
     * javascriptTemplate.execute("repeatSubmission", 2, () -> {
     * return RestResult.success();
     * })
     * @params [key: key, expire: 重复提交时间间隔 秒, executeMethod: 执行方法]
     * @Author mingyi ge
     * @Date 2020/9/30 15:25
     */
    public <T> T execute(String key, long expire, ExecuteMethod<T> executeMethod) {
        return execute(key, expire, executeMethod, true);
    }

    /**
     * @Description 防止重复提交
     * @params [key: key, expireMS: 超时时间, executeMethod: 执行函数, isAutoReleaseLock: 自动释放锁]
     * @Author mingyi ge
     * @Date 2020/9/30 16:18
     */
    private <T> T execute(String key, long expireMS, ExecuteMethod<T> executeMethod, boolean isAutoReleaseLock) {
        Assert.hasText(key, "redis key不能为空");
        String identifier = redisLock.acquireLock(key, expireMS);
        if (StringUtils.isNotBlank(identifier)) {
            try {
                return executeMethod.doExecute();
            } finally {
                if (isAutoReleaseLock) {
                    redisLock.releaseLock(key, identifier);
                }
            }
        } else {
            log.info("###key已存在，打断 key={}", key);
            throw new RuntimeException("接口重复请求");
        }
    }


    //---------------------------- limit ----------------------------


    public <T> T limit(Class<?> clazz, String methodName, Class<?>[] params, ExecuteMethod<T> executeMethod) {
        Method method = null;
        try {
            method = clazz.getMethod(methodName, params);
        } catch (NoSuchMethodException e) {
            log.error("###key限流，method not fund key={}", executeMethod);
            throw new RuntimeException("method not fund");
        }

        //
        RequestRateLimiter requestRateLimiter = method.getAnnotation(RequestRateLimiter.class);
        System.out.println(requestRateLimiter);

        RRateLimiter rRateLimiter = limit(requestRateLimiter);
        if (rRateLimiter.tryAcquire(1)) {
            return executeMethod.doExecute();
        } else {
            log.info("###key限流，打断 key={}", rRateLimiter);
            throw new RuntimeException("接口被限流");
        }
    }

    private RRateLimiter limit(RequestRateLimiter requestRateLimiter) {
        RRateLimiter rRateLimiter = redissonClient.getRateLimiter(StringUtils.isBlank(requestRateLimiter.key()) ? "default:limiter" : requestRateLimiter.key());
        // 设置限流
        if (rRateLimiter.isExists()) {
            RateLimiterConfig rateLimiterConfig = rRateLimiter.getConfig();
            // 判断配置是否更新，如果更新，重新加载限流器配置
            if (!Objects.equals(requestRateLimiter.rate(), rateLimiterConfig.getRate())
                    || !Objects.equals(requestRateLimiter.timeUnit().toMillis(requestRateLimiter.rateInterval()), rateLimiterConfig.getRateInterval())
                    || !Objects.equals(requestRateLimiter.type(), rateLimiterConfig.getRateType())) {
                rRateLimiter.delete();
                rRateLimiter.trySetRate(requestRateLimiter.type(), requestRateLimiter.rate(), requestRateLimiter.rateInterval(), requestRateLimiter.timeUnit());
            }
        } else {
            rRateLimiter.trySetRate(requestRateLimiter.type(), requestRateLimiter.rate(), requestRateLimiter.rateInterval(), requestRateLimiter.timeUnit());
        }
        return rRateLimiter;
    }

}
