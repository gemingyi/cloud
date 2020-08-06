package com.example.gatewayserver.filters;

import com.alibaba.fastjson.JSON;
import com.example.commons.enums.ResultCode;
import com.example.commons.result.RestResult;
import com.example.commons.utils.JWTUtil;
import com.example.gatewayserver.feign.AuthFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 微服务网关鉴权
 */
public class AuthFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZATION = "Authorization";
    private static final String ANONYMOUS_PATH = "/uaa/auth/login, /uaa/auth/refresh, /uaa/auth/check, /uaa/auth/info, /baidu";

    @Autowired
    private AuthFeign authFeign;

    private AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        //匿名访问url
        boolean flag = this.isAnonymousPath(path);
        if(flag) {
            return chain.filter(exchange);
        }
        //未传token
        String token = request.getHeaders().getFirst(AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            return this.error(response);
        }
        //调用鉴权服务 判断token
        RestResult<Object> result = authFeign.info(token);
        if (!ResultCode.SUCCESS.code().equals(result.getCode())) {
            return this.error(response);
        }
        JWTUtil.getClaim(token, "roles");

        //鉴权通过 将登录信息传递到下级服务
        ServerHttpRequest newRequest = request.mutate().header("subject", result.getData().toString()).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }


    /**
     * 匿名访问路径
     */
    private boolean isAnonymousPath(String path) {
        String[] urls = ANONYMOUS_PATH.split(", ");
        for (String url : urls) {
            if (pathMatcher.match(path, url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 无权限返回信息
     */
    private Mono<Void> error(ServerHttpResponse response) {
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        RestResult<ResultCode> result = RestResult.failure(ResultCode.NO_PERMISSION);
        String restResultStr = JSON.toJSONString(result);
        DataBuffer buffer = response.bufferFactory().wrap(restResultStr.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

}
