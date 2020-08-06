package com.example.gatewayserver.swagger;

import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.*;

@Component
@RefreshScope
public class SwaggerResourceProvider implements SwaggerResourcesProvider {
    public static final String URL = "/v2/api-docs";

    private RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String projectName;

    /**
     * 项目中英文对应关系列表(自定义属性)
     */
    @Value("${spring.application.names:}")
    private String projectNames;

    /**
     * 是否启用网关聚合服务列表
     */
    @Value("${swagger.enable:true}")
    private boolean enableSwagger = true;

    @Autowired
    public SwaggerResourceProvider(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        //是否展示swagger接口列表，这里返回空集合就表示不展示swagger接口列表
        if (!enableSwagger) {
            return resources;
        }
        Map<String, String> routeHosts = new LinkedHashMap<>();
        // 由于我的网关采用的是负载均衡的方式，因此我需要拿到所有应用的serviceId
        // 获取所有可用的host：serviceId
        routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null)
                .filter(route -> !projectName.equals(route.getUri().getHost()))
                .subscribe(route -> routeHosts.putIfAbsent(route.getId(), route.getId()));

        Map<String, String> names = new LinkedHashMap<>();
        //解析配置好的项目中英文描述对应关系
        if (!StringUtils.isEmpty(projectNames)) {
            names = Splitter.onPattern(",|，|;|；").omitEmptyStrings().trimResults().withKeyValueSeparator(":").split(projectNames);
        }
        // 记录已经添加过的server，存在同一个应用注册了多个服务在nacos上
        Set<String> dealed = new HashSet<>();
        for (Map.Entry<String, String> entry : routeHosts.entrySet()) {
            String instance = entry.getKey();//服务名称
            String desc = entry.getValue();//服务中文名描述
            desc = names.getOrDefault(instance, desc);
            // 拼接url，样式为/serviceId/v2/api-info，当网关调用这个接口时，会自动通过负载均衡寻找对应的主机
            String url = "/" + instance + URL;
            if (!dealed.contains(url)) {
                dealed.add(url);
                SwaggerResource resource = new SwaggerResource();
                resource.setUrl(url);
                resource.setName(desc);
                resource.setSwaggerVersion("1.0.0");
                resource.setLocation(url);
                resources.add(resource);
            }
        }
        return resources;
    }
}
