package com.example.pluginredis.util;

import com.example.pluginredis.constant.RedisKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;


/**
 * redis 锁
 */
@Slf4j
public class RedisLock {

    @Autowired
    RedisScript<Long> lockScript;
    @Autowired
    private RedisScript<Long> unlockScript;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 非阻塞锁加锁
     *
     * @param lockName   锁的namel
     * @param expire    锁的过期时间(秒)
     * @return 解锁 identifier
     */
    public String acquireLock(String lockName, long expire) {
        String identifier = this.generateIdentifier();
        String lockKey = RedisKeyConstant.LOCK_NAME_PREFIX + lockName;
        try {
            Long result = redisTemplate.execute(lockScript, Collections.singletonList(lockKey), identifier, String.valueOf(expire));
            log.info("distributedLock.key{}: - identifier:{}: - result:{} - expire:{}", lockKey, identifier, result, expire);
            if (!StringUtils.isEmpty(result) && result == 1) {
                return identifier;
            }
            return null;
        } catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }

    /**
     * 阻塞锁加锁
     *
     * @param lockName   锁的key
     * @param expire    锁的过期时间（秒）
     * @param acquireTimeOut 获取锁的等待时间(秒)，超过这个时间获取失败
     * @return 解锁 identifier
     */
    public String acquireLockWithTimeOut(String lockName, long expire, long acquireTimeOut) {
        String identifier = this.generateIdentifier();
        String lockKey = RedisKeyConstant.LOCK_NAME_PREFIX + lockName;
        try {
            long endTime = System.currentTimeMillis() + acquireTimeOut * 1000;
            while (System.currentTimeMillis() < endTime) {
                Long result = redisTemplate.execute(lockScript, Collections.singletonList(lockKey), identifier, String.valueOf(expire));
                if (!StringUtils.isEmpty(result) && result == 1) {
                    return identifier;
                }
                //防止一直消耗 CPU
                Thread.sleep(100);
            }
        } catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }

    /**
     * 解锁
     *
     * @param lockName   锁的name
     * @param identifier 锁的唯一ID
     */
    public boolean releaseLock(String lockName, String identifier) {
        String lockKey = RedisKeyConstant.LOCK_NAME_PREFIX + lockName;
        Long result = redisTemplate.execute(unlockScript, Collections.singletonList(lockKey), identifier);
        log.info("distributedLock.key{}: - uuid:{}: - result:{}", lockKey, identifier, result);
        if (!StringUtils.isEmpty(result) && result == 1) {
            return true;
        }
        return false;
    }

    /**
     * 生成key UUID
     */
    private String generateIdentifier() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return UUID.randomUUID().toString().replace("-", "");
    }

}
