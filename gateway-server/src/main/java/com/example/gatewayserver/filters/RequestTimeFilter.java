package com.example.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 全局请求耗时统计
 */
public class RequestTimeFilter implements GlobalFilter, Ordered {

    private final static Logger log = LoggerFactory.getLogger(RequestTimeFilter.class);


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();

        return chain.filter(exchange).then().then(Mono.fromRunnable(() -> {
            long endTime = System.currentTimeMillis();
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();
            String method = request.getMethodValue();
            String remoteIp = request.getRemoteAddress().getHostString();
//            Map<String, String> paramsMap = request.getQueryParams().toSingleValueMap();
//            String params = JSON.toJSONString(paramsMap.isEmpty() ? "" : paramsMap);
            log.info("请求来源地址[{}], 请求路径[{}]，请求方式[{}], 请求耗时[{}]", remoteIp, path, method, (endTime - startTime));
        }));
    }

    @Override
    public int getOrder() {
        return -100;
    }

}
