package com.example.pluginredis.javascript;

import com.example.pluginredis.util.RedisLock;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
public class JavascriptTemplate {

    @Autowired
    private RedisLock redisLock;


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

}
