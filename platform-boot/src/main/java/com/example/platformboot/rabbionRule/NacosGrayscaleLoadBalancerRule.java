package com.example.platformboot.rabbionRule;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Description: 自定义feign灰度调用 rabbion负载
* 子项目开启 gray.open=true
* @author mingyi ge
* @date 2020/12/9 18:11
*/
@Slf4j
@Component
@ConditionalOnProperty(prefix = "gray", value = "open", havingValue = "true")
public class NacosGrayscaleLoadBalancerRule extends AbstractLoadBalancerRule {

    @Value("${gray.api.version.mark:api-version}")
    private String apiVersion;
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {}

    @Override
    public Server choose(Object o) {
        //获取集群名称
        String clusterName = this.nacosDiscoveryProperties.getClusterName();
        //获取当前实例 自定义元数据-api版本
        String version = this.nacosDiscoveryProperties.getMetadata().get(apiVersion);

        DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer) getLoadBalancer();
        String name = loadBalancer.getName();
        NamingService namingService = this.nacosDiscoveryProperties.namingServiceInstance();
        try {
            // 所有 被调用实例
            List<Instance> instances = namingService.selectInstances(name, true);
            if (CollectionUtils.isEmpty(instances)) {
                log.warn("no instance in service {}", clusterName);
                return null;
            }

            // 筛选指定版本 version(为空筛选为空的实例)
            List<Instance> metadataMatchInstances = screenVersion(instances, version);

            List<Instance> clusterMetadataMatchInstances = metadataMatchInstances;
            // 如果配置了集群名称，需筛选同集群下元数据匹配的实例
            if (StringUtils.isNotBlank(clusterName)) {
                clusterMetadataMatchInstances = metadataMatchInstances.stream()
                        .filter(instance -> clusterName.equals(instance.getClusterName()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(clusterMetadataMatchInstances)) {
                    clusterMetadataMatchInstances = metadataMatchInstances;
                    log.warn("发生跨集群调用。clusterName = {}, version = {}, clusterMetadataMatchInstances = {}", clusterName, version, clusterMetadataMatchInstances);
                }
            }

            Instance instance = ExtendBalancer.getHostByRandomWeight2(clusterMetadataMatchInstances);
            return new NacosServer(instance);
        } catch (Exception e) {
            log.error("服务:{}, 自定义灰度feign ribbon异常", name);
            return null;
        }
    }

    /**
     * 筛选想要的版本
     * @param instances 服务实列
     * @param version   版本
     */
    private List<Instance> screenVersion(List<Instance> instances, String version) {
        // version为空不筛选
//        if (StringUtils.isBlank(version)) {
//            return instances;
//        }
        // 进行按版本分组排序(version为空)
        Map<String, List<Instance>> versionMap = groupInstanceByVersion(instances);

        return versionMap.get(version);
    }

    /**
     * 根据version 组装一个map key value  对应 version List<Instance>
     * @param instances 服务实列
     */
    private Map<String, List<Instance>> groupInstanceByVersion(List<Instance> instances) {
        Map<String, List<Instance>> versionMap = new HashMap<>(instances.size());
        for (Instance instance : instances) {
            String version = instance.getMetadata().get(apiVersion);
            List<Instance> versions = versionMap.get(version);
            if (versions == null) {
                versions = new ArrayList<>();
            }
            versions.add(instance);
            versionMap.put(version, versions);
        }
        return versionMap;
    }

}
