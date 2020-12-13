package com.example.gatewayserver.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.commons.result.RestResult;
import com.example.commons.result.ResultCode;
import com.example.commons.utils.AliJsonUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * sentinel 处理
 */
public class GlobalSentinelExceptionHandler extends SentinelGatewayBlockExceptionHandler {


    public GlobalSentinelExceptionHandler(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        super(viewResolvers, serverCodecConfigurer);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        if (!BlockException.isBlockException(ex)) {
            return Mono.error(ex);
        }
        return handleBlockedRequest(exchange, ex).flatMap(response -> writeResponse(exchange));
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange) {
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        byte[] datas = AliJsonUtil.objectToJsonStr(RestResult.failure(ResultCode.INTERFACE_EXCEED_LOAD)).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(datas);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    private Mono<ServerResponse> handleBlockedRequest(ServerWebExchange exchange, Throwable throwable) {
        return GatewayCallbackManager.getBlockHandler().handleRequest(exchange, throwable);
    }

}
