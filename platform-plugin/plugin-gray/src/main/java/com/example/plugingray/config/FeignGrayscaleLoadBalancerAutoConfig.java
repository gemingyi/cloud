package com.example.plugingray.config;

import com.example.plugingray.properties.GrayscaleLoadProperties;
import com.example.plugingray.service.FeignHystrixConcurrencyStrategy;
import com.example.plugingray.service.FeignNacosGrayscaleLoadBalancerRule;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.loadbalancer.IRule;
import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Configuration
@EnableConfigurationProperties(GrayscaleLoadProperties.class)
@ConditionalOnProperty(prefix = "loverent.gray", value = "service-open", havingValue = "true")
public class FeignGrayscaleLoadBalancerAutoConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeignGrayscaleLoadBalancerAutoConfig.class);


    /**
     * feign拦截器
     */
    @Bean
    @Primary
    public RequestInterceptor headerInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                //
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        template.header(name, values);
                    }
                }

            }
        };
    }

    /**
     * 自定义并发策略
     */
    @Bean
    public HystrixConcurrencyStrategy feignHystrixConcurrencyStrategy() {
        return new FeignHystrixConcurrencyStrategy();
    }

    //-------------------------------------------------------------

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
