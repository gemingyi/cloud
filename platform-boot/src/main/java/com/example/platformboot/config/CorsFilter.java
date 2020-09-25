package com.example.platformboot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsFilter {

//    @Bean
//    public FilterRegistrationBean corsFilter(corsProperties corsProperties) {
//        log.debug("corsProperties:{}", corsProperties);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        // 跨域配置
//        corsConfiguration.setAllowedOrigins(corsProperties.getAllowedOrigins());
//        corsConfiguration.setAllowedHeaders(corsProperties.getAllowedHeaders());
//        corsConfiguration.setAllowedMethods(corsProperties.getAllowedMethods());
//        corsConfiguration.setAllowCredentials(corsProperties.isAllowCredentials());
//        corsConfiguration.setExposedHeaders(corsProperties.getExposedHeaders());
//        corsConfiguration.setMaxAge(corsConfiguration.getMaxAge());
//
//        source.registerCorsConfiguration(corsProperties.getPath(), corsConfiguration);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        bean.setEnabled(corsProperties.isEnable());
//        return bean;
//    }
//
    @Bean
    public FilterRegistrationBean<org.springframework.web.filter.CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", buildConfig()); //CORS 配置对所有接口都有效
        FilterRegistrationBean<org.springframework.web.filter.CorsFilter> bean = new FilterRegistrationBean<>(new org.springframework.web.filter.CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1
        corsConfiguration.addAllowedHeader("*"); // 2
        corsConfiguration.addAllowedMethod("*"); // 3
        return corsConfiguration;
    }

}
