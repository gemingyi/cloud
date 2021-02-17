package com.example.platformboot.config;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
* @Description feign调用传递请求头
* @Author mingyi ge
* @Date 2020/9/9 20:36
*/
@Configuration
/**
 * 没有引入gray依赖 && 没有开启service灰度
 */
@ConditionalOnMissingClass("com.example.plugingray.config.FeignGrayscaleLoadBalancerAutoConfig")
@ConditionalOnProperty(prefix = "loverent.gray", value = "service-open", havingValue = "false")
public class FeignHeaderConfig {

    /**
    * @Description feign向下游服务传递 请求头
    * @Author mingyi ge
    * @Date 2020/9/13 14:20
    */
    @Bean
    public RequestInterceptor headerInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
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

}
