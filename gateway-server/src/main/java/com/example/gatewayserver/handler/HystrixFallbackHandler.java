//package com.example.gatewayserver.handler;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.HandlerFunction;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//import java.util.Optional;
//
//import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
//
///**
// * Hystrix 降级处理
// */
//@Component
//public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {
//
//    private final static Logger log = LoggerFactory.getLogger(HystrixFallbackHandler.class);
//
//
//    @Override
//    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
//        Optional<Object> originalUris = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
//
//        originalUris.ifPresent(originalUri -> log.error("网关执行请求:{}失败,hystrix服务降级处理", originalUri));
//
//        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromObject("服务异常"));
//    }
//}