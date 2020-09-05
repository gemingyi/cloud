package com.example.gatewayserver.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

@Component
public class NacosDynamicRouterListener implements ApplicationEventPublisherAware {

    private final static Logger log = LoggerFactory.getLogger(NacosDynamicRouterListener.class);


    @Value("${spring.cloud.nacos.dynamic-route.data-id:}")
    private String dataId;

    @Value("${spring.cloud.nacos.discovery.group:}")
    private String group;

    @Value("${spring.cloud.nacos.discovery.namespace:}")
    private String namespace;

    @Value("${spring.cloud.nacos.discovery.server-addr:127.0.0.1:}")
    private String serverAddr;


    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    private static List<String> routeIds = Collections.synchronizedList(new ArrayList<>());


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Bean
    public void dynamicRouteByNacosListener() {
        try {
            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverAddr);
            //如果启用了命名空间，这个命名空间一定不能少，不然动态更新不生效
            properties.setProperty(PropertyKeyConst.NAMESPACE, namespace);
            ConfigService configService = NacosFactory.createConfigService(properties);

            String configInfo = configService.getConfig(dataId, group, 5000);
            log.info("获取网关当前配置: {}", configInfo);
            this.updateRoute(configInfo);
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    updateRoute(configInfo);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 更新路由配置
     */
    private void updateRoute(String configInfo) {
        try {
            this.clearRoute();
            List<RouteDefinition> definitions = JSONObject.parseArray(configInfo, RouteDefinition.class);
            if (null != definitions && !definitions.isEmpty()) {
                for (RouteDefinition definition : definitions) {
                    this.addRoute(definition);
                }
                this.publish();
                log.info("更新网关路由配置：{}", JSON.toJSONString(definitions));
            }
        } catch (Exception e) {
            log.warn(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 清空路由
     */
    private void clearRoute() {
        for (String id : routeIds) {
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
        }
        routeIds.clear();
    }

    /**
     * 添加路由
     */
    private void addRoute(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        routeIds.add(definition.getId());
    }

    /**
     * 发布
     */
    private void publish() {
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this.routeDefinitionWriter));
    }
}
