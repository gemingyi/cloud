package com.example.platformboot.config;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
* @Description feign
* @Author mingyi ge
* @Date 2020/9/9 20:36
*/
@Configuration
// gray 里面已经有一个配置了
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
