package com.example.userserver;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.example.platformboot.BootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;


import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

//@SpringBootApplication
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients
@MapperScan("com.example.userserver.dao")
public class UserServerApplication extends BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }


    @Configuration
    class DataSourceConfiguration  {

        @Value("${mybatis-plus.mapper-locations}")
        private String mapperLocations;


        @Bean
        @ConfigurationProperties(prefix = "spring.datasource")
        public DataSource druidDataSource(){
            DruidDataSource druidDataSource = new DruidDataSource();
            return druidDataSource;
        }

        @Primary
        @Bean("dataSource")
        public DataSourceProxy dataSource(DataSource druidDataSource){
            return new DataSourceProxy(druidDataSource);
        }

        @Bean
        public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy)throws Exception{
            MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSourceProxy);
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
            sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
            return sqlSessionFactoryBean.getObject();
        }
    }

}
