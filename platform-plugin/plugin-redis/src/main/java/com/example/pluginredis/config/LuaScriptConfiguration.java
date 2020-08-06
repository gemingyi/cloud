package com.example.pluginredis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
@ConditionalOnClass({RedisOperations.class})
@EnableConfigurationProperties({RedisProperties.class})
public class LuaScriptConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(LuaScriptConfiguration.class);


    /**
     * 加锁脚本
     */
    @Bean(name = "lockScript")
    public RedisScript<Long> lockScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        try {
            ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("/script/lock.lua"));
            redisScript.setScriptSource(scriptSource);
            redisScript.setResultType(Long.class);
        } catch (Exception e) {
            logger.error("error" , e);
        }
        return redisScript;
    }

    /**
     * 解锁脚本
     */
    @Bean(name = "unlockScript")
    public RedisScript<Long> unlockScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        try {
            ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("/script/unlock.lua"));
            redisScript.setScriptSource(scriptSource);
            redisScript.setResultType(Long.class);
        } catch (Exception e) {
            logger.error("error" , e);
        }
        return redisScript;
    }

    /**
     * 限流脚本
     */
    @Bean(name = "limitScript")
    public RedisScript<Long> limitScript() {
        RedisScript<Long> redisScript = null;
        try {
            ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("/script/limit.lua"));
            redisScript = RedisScript.of(scriptSource.getScriptAsString(), Long.class);
        } catch (Exception e) {
            logger.error("error", e);
        }
        return redisScript;
    }

}
