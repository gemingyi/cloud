package com.example.userserver.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/9/1 10:05
 */
@Configuration
public class RedissonConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedissonConfig.class);


    @Bean
    public RedissonClient redissionClient() throws IOException {

        //单机模式
        RedissonClient redisson = Redisson.create(Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream()));

        return redisson;

        //集群模式
        /*config.useClusterServers()
                .addNodeAddress("redis://192.168.56.101:36379")
                .addNodeAddress("redis://192.168.56.102:36379")
                .addNodeAddress("redis://192.168.56.103:36379")
                .setPassword("1111111")
                .setScanInterval(5000);*/

        //哨兵模式
        /*config.useSentinelServers().addSentinelAddress("redis://ip1:port1",
                "redis://ip2:port2",
                "redis://ip3:port3")
                .setMasterName("mymaster")
                .setPassword("password")
                .setDatabase(0);*/

//        RedissonClient redissonClient = Redisson.create(config);
//        return redissonClient;
    }
}
