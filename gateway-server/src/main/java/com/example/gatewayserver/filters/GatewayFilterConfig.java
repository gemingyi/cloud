package com.example.gatewayserver.filters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayFilterConfig {

    @Bean
    public AuthFilter tokenFilter(){
        return new AuthFilter();
    }

    @Bean
    public RequestTimeFilter requestTimeFilter() {
        return new RequestTimeFilter();
    }
}
