package com.example.plugingray.config;


import com.example.plugingray.gateway.GatewayLoadBalancerClientFilter;
import com.example.plugingray.gateway.GatewayNacosGrayscaleLoadBalancerRule;
import com.example.plugingray.properties.GrayscaleLoadProperties;
import com.netflix.loadbalancer.IRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(GrayscaleLoadProperties.class)
@ConditionalOnProperty(prefix = "loverent.gray", value = "gateway-open", havingValue = "true")
// 不加这个会被覆盖
@AutoConfigureBefore({GatewayLoadBalancerClientAutoConfiguration.class})
public class GatewayGrayscaleLoadBalancerAutoConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(GatewayGrayscaleLoadBalancerAutoConfig.class);

    /**
     * @Description: 自定义网关 灰度路由 config
     * @author mingyi ge
     * @date 2020/12/14 11:15
     */
    @Bean
    @Primary
    public LoadBalancerClientFilter gatewayLoadBalancerClientFilter(LoadBalancerClient loadBalancerClient, LoadBalancerProperties loadBalancerProperties) {
        GatewayLoadBalancerClientFilter gatewayLoadBalancerClientFilter = new GatewayLoadBalancerClientFilter(loadBalancerClient, loadBalancerProperties);
        return gatewayLoadBalancerClientFilter;
    }

    /**
     * @Description: 自定义网关 灰度路由 config
     * @author mingyi ge
     * @date 2020/12/14 11:15
     */
    @Bean
    public IRule gatewayNacosGrayscaleLoadBalancerRule() {
        LOGGER.info("###### 已启动gateway灰度路由starter ######");
        return new GatewayNacosGrayscaleLoadBalancerRule();
    }

}
