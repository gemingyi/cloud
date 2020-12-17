package com.example.plugingray.config;

import com.example.plugingray.properties.GrayscaleLoadProperties;
import com.example.plugingray.service.FeignNacosGrayscaleLoadBalancerRule;
import com.netflix.loadbalancer.IRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GrayscaleLoadProperties.class)
@ConditionalOnProperty(prefix = "loverent.gray", value = "service-open", havingValue = "true")
public class FeignGrayscaleLoadBalancerAutoConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeignGrayscaleLoadBalancerAutoConfig.class);

    /**
     * @Description: 自定义服务feign调用 灰度路由 config
     * @author mingyi ge
     * @date 2020/12/14 10:03
     */
    @Bean
    public IRule nacosGrayscaleRule() {
        LOGGER.info("###### 已启动service灰度路由starter ######");
        return new FeignNacosGrayscaleLoadBalancerRule();
    }

}
