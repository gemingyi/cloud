package com.example.pluginredis.util;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis bool过滤器
 * 网上抄的 https://www.jianshu.com/p/a0cf6171806a
 */
public class RedisBloomFilter {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public <T> void addByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            redisTemplate.opsForValue().setBit(key, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean includeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            if (!redisTemplate.opsForValue().getBit(key, i)) {
                return false;
            }
        }
        return true;
    }

}
