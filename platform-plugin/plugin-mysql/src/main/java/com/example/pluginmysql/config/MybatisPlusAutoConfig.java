package com.example.pluginmysql.config;


import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.example.pluginmysql.strengthen.CommonSqlInjector;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;


/**
 * mybatis-plus 自动装配
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@ConditionalOnClass({HikariDataSource.class, Driver.class})
public class MybatisPlusAutoConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(MybatisPlusAutoConfig.class);


    /**
     * 分页 拦截器
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        LOGGER.info("###### 已加载Mybatis-Plus分页拦截器 ######");
        return new PaginationInterceptor();
    }

    /**
     * explain 拦截器
     */
    @Bean
    @ConditionalOnProperty(prefix = "mybatis-plugin.explain" , havingValue = "true" , name = "enabled")
    public SqlExplainInterceptor sqlExplainInterceptor() {
        SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        sqlParserList.add(new BlockAttackSqlParser());
        sqlExplainInterceptor.setSqlParserList(sqlParserList);
        LOGGER.info("###### 已加载Mybatis-Plus explain拦截器 ######");
        return sqlExplainInterceptor;
    }


    //---------------------------------------------------------------------------------------------

    /**
     * 增强增强IBaseMapper
     */
//    @Bean
//    public CommonSqlInjector commonSqlInjector() {
//        LOGGER.info("###### 增强IBaseMapper, 添加batchInsert ######");
//        return new CommonSqlInjector();
//    }

}
