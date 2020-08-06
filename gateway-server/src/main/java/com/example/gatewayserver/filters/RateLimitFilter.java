//package com.example.gatewayserver.filters;
//
//import com.example.commons.enums.ResultCode;
//import com.example.commons.result.RestResult;
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
//import com.google.common.util.concurrent.RateLimiter;
//import com.google.gson.Gson;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//
///**
// * 网关限流
// */
//public class RateLimitFilter implements GlobalFilter, Ordered {
//
//    public static final int REQUEST_COUNT = 50;
//    // 根据IP分不同的令牌桶, 每天自动清理缓存
//    private static LoadingCache<String, RateLimiter> caches = CacheBuilder.newBuilder()
//            .maximumSize(1000).expireAfterWrite(1, TimeUnit.DAYS)
//            .build(new CacheLoader<String, RateLimiter>() {
//                @Override
//                public RateLimiter load(String key) throws Exception {
//                    // 新的IP初始化 (限流每秒两个令牌响应)
//                    return RateLimiter.create(REQUEST_COUNT);
//                }
//            });
//
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
//        try {
//            RateLimiter rateLimiter = caches.get(ip);
//            if (rateLimiter.tryAcquire()) {
//                return chain.filter(exchange);
//            } else {
//                return this.error(exchange.getResponse());
//            }
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return chain.filter(exchange);
//    }
//
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//
//    private Mono<Void> error(ServerHttpResponse response) {
//        //返回错误
//        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//        Gson gson = new Gson();
//        RestResult<ResultCode> result = RestResult.failure(ResultCode.INTERFACE_EXCEED_LOAD);
//        String restResultStr = gson.toJson(result);
//        DataBuffer buffer = response.bufferFactory().wrap(restResultStr.getBytes(StandardCharsets.UTF_8));
//        return response.writeWith(Mono.just(buffer));
//    }
//}
