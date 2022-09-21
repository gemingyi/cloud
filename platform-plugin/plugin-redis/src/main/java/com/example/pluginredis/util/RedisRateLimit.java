package com.example.pluginredis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * https://www.codetd.com/article/13613671
 * https://www.dazhuanlan.com/xuning690/topics/1420513
 * https://www.zhangshengrong.com/p/Z9a23KkmNV/
 * https://segmentfault.com/a/1190000019477128?utm_medium=referral&utm_source=tuicool
 * redis 限流
 */
public class RedisRateLimit {

    //分布式锁，前缀
    private String LIMIT_NAME_PREFIX = "limit:";

    @Autowired
    RedisScript<Long> limitScript;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     *
     * @param limitKey  key
     * @param rate  频率
     * @param timeInterval  时间间隔 秒
     */
    public Boolean limit(String limitKey, long rate, long timeInterval) {
        Long result = 0L;
        String lockKey = this.LIMIT_NAME_PREFIX + limitKey;
        try {
            result = redisTemplate.execute(limitScript, Collections.singletonList(lockKey), String.valueOf(rate), String.valueOf(timeInterval));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //达到阈值
        if (!StringUtils.isEmpty(result) && result == 0L) {
            return false;
        }
        return true;
    }

}
