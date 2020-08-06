package com.example.pluginredis.config;

import com.example.pluginredis.util.RedisBloomFilter;
import com.example.pluginredis.util.RedisLock;
import com.example.pluginredis.util.RedisRateLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnClass({RedisOperations.class})
@EnableConfigurationProperties({RedisProperties.class})
public class RedisAutoConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisAutoConfig.class);


    @Bean(name = "redisTemplate")
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        LOGGER.info("###### 已加载RedisTemplate ######");
        return redisTemplate;
    }


    @Bean(name = "redisLock")
    RedisLock redisLock() {
        return new RedisLock();
    }

    @Bean(name = "redisRateLimit")
    RedisRateLimit redisRateLimit() {
        return new RedisRateLimit();
    }

    @Bean(name = "redisBloomFilter")
    RedisBloomFilter redisBloomFilter() {
        return new RedisBloomFilter();
    }

}
