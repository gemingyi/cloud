package com.example.pluginmysql.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.example.pluginmysql.util.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义雪花算法
 */
@Configuration
public class MybatisGeneratorIdConfig {

    @Bean
    public BaseIdentifierGenerator myIdentifierGenerator() {
        return new BaseIdentifierGenerator();
    }

}

/**
 * 自定义 雪花ID
 */
class BaseIdentifierGenerator implements IdentifierGenerator {
    IdWorker idWorker = new IdWorker(null);

    @Override
    public Number nextId(Object entity) {
        return idWorker.nextId();
    }
}