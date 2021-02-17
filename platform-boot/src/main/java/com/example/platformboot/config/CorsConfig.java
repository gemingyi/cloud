package com.example.platformboot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Description 全局跨域配置
 * @Author mingyi ge
 * @Date 2020/9/25 17:29
 */
@Configuration
public class CorsConfig {

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

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        //CORS 配置对所有接口都有效
        source.registerCorsConfiguration("/**", buildConfig());
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        // 这个顺序很重要哦，为避免麻烦请设置在最前
        bean.setOrder(0);
        return bean;
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //
        corsConfiguration.addAllowedOrigin("*");
        //
        corsConfiguration.addAllowedHeader("*");
        //
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

}
