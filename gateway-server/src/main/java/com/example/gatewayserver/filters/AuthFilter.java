package com.example.gatewayserver.filters;

import com.alibaba.fastjson.JSON;
import com.example.commons.result.ResultCode;
import com.example.commons.result.RestResult;
import com.example.commons.jwt.JWTUtil;
import com.example.gatewayserver.feign.AuthFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    /**
     * 是否校验token配置
     */
    @Value("${gateway.check.token:false}")
    private boolean checkToken;

    /**
     * 忽略校验token的路径
     */
    @Value("${gateway.check.ignoreToken.path:}")
    private String ignoreTokenPath;

    /**
     * staticResource的路径
     */
    @Value("${gateway.check.staticResource.path:}")
    private String staticResourcePath;


    /**
     * 服务鉴权 head
     */
    private static final String AUTHORIZATION = "Authorization";


    @Autowired
    private AuthFeign authFeign;

    private AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        //放行访问url
        boolean flag = this.isPassThroughPath(path);
        if (!checkToken || flag) {
            return chain.filter(exchange);
        }
        //未传token
        String token = request.getHeaders().getFirst(AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            return this.serviceUnauthorized(response);
        }
        //调用鉴权服务 判断请求token
        RestResult<Object> result = authFeign.info(token);
        if (!ResultCode.SUCCESS.code().equals(result.getCode())) {
            return this.serviceUnauthorized(response);
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
     * 放行路径
     */
    private boolean isPassThroughPath(String path) {
        //匿名
        String[] urls = ignoreTokenPath.split(", ");
        for (String url : urls) {
            if (pathMatcher.match(url, path)) {
                return true;
            }
        }
        //静态
        String[] urls2 = staticResourcePath.split(", ");
        for (String url : urls2) {
            if (pathMatcher.match(url, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 服务调用未认证 相应数据
     */
    private Mono<Void> serviceUnauthorized(ServerHttpResponse response) {
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        RestResult<ResultCode> result = RestResult.failure(ResultCode.NO_PERMISSION);
        String restResultStr = JSON.toJSONString(result);
        DataBuffer buffer = response.bufferFactory().wrap(restResultStr.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

}
