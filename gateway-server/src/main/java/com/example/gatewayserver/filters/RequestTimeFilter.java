package com.example.gatewayserver.filters;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;


/**
 * 全局请求耗时统计
 */
@Slf4j
@Component
public class RequestTimeFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Stopwatch start = Stopwatch.createStarted();

        return chain.filter(exchange).then().then(Mono.fromRunnable(() -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();
            String method = request.getMethodValue();
            String remoteIp = request.getRemoteAddress().getHostString();
            long millis = start.stop().elapsed(TimeUnit.MILLISECONDS);
//            Map<String, String> paramsMap = request.getQueryParams().toSingleValueMap();
//            String params = JSON.toJSONString(paramsMap.isEmpty() ? "" : paramsMap);
            log.info("请求来源地址[{}], 请求路径[{}]，请求方式[{}], 请求耗时[{}]", remoteIp, path, method, millis);
        }));
    }

    @Override
    public int getOrder() {
        return -100;
    }

}
