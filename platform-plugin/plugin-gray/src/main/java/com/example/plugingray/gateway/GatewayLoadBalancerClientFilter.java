package com.example.plugingray.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

/**
 * @author mingyi ge
 * @Description: 网关灰度发布过滤器 向RibbonLoadBalancerClient 添加一些信息（GrayscaleLoadBalancerRule类 好根据信息 负载路由）
 * @date 2020/12/10 11:59
 */
public class GatewayLoadBalancerClientFilter extends LoadBalancerClientFilter {

    @Value("${loverent.gray.api-version-mark:api-version}")
    private String apiVersion;

    public GatewayLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {

        if (this.loadBalancer instanceof RibbonLoadBalancerClient) {
            RibbonLoadBalancerClient client = (RibbonLoadBalancerClient) this.loadBalancer;
            HttpHeaders headers = exchange.getRequest().getHeaders();
            // 前端请求头 version
            String version = headers.getFirst(apiVersion);
            //
            String serviceId = ((URI) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR)).getHost();
            GrayscaleProperties build = new GrayscaleProperties();
            build.setVersion(version);
            build.setServerName(serviceId);
            // 这里使用服务ID 和 version 做为选择服务实例的key
            return client.choose(serviceId, build);
        }
        return super.choose(exchange);
    }

}
