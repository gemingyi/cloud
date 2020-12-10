package com.example.gatewayserver.filters;

import lombok.Data;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.io.Serializable;
import java.net.URI;

/**
* @Description: 网关灰度发布过滤器 向下游传递一些信息（GrayscaleLoadBalancerRule 好根据信息判断）
* @author mingyi ge
* @date 2020/12/10 11:59
* @params
*/
@Component
public class GatewayLoadBalancerClientFilter extends LoadBalancerClientFilter {

    public GatewayLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }
    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {

        if (this.loadBalancer instanceof RibbonLoadBalancerClient) {
            RibbonLoadBalancerClient client = (RibbonLoadBalancerClient) this.loadBalancer;
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String version = headers.getFirst("api-version");
            String serviceId = ((URI) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR)).getHost();
//            GrayscaleProperties build = GrayscaleProperties.builder().version( version ).serverName( serviceId ).build();
            GrayscaleProperties build = new GrayscaleProperties();
            build.setVersion(version);
            build.setServerName(serviceId);
            //这里使用服务ID 和 version 做为选择服务实例的key
            //TODO 这里也可以根据实际业务情况做自己的对象封装
            return client.choose(serviceId, build);
        }
        return super.choose(exchange);
    }

    @Data
    class GrayscaleProperties implements Serializable {
        private String version;
        private String serverName;
        private String serverGroup;
        private String active;
        private double weight = 1.0D;
    }

}
