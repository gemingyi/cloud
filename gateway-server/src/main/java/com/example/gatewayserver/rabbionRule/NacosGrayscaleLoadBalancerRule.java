package com.example.gatewayserver.rabbionRule;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.example.gatewayserver.properties.GrayscaleProperties;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
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
 * @author mingyi ge
 * @Description: 自定义网关灰度发布负载
 * @date 2020/12/10 11:57
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
    public Server choose(Object key) {
        try {
            String clusterName = this.nacosDiscoveryProperties.getClusterName();
            // GatewayLoadBalancerClientFilter里面传递的数据
            GrayscaleProperties grayscale = (GrayscaleProperties) key;
            // 请求头 version
            String version = grayscale.getVersion();
            //
            String serverName = grayscale.getServerName();
            NamingService namingService = this.nacosDiscoveryProperties.namingServiceInstance();
            // 所有 被调用实例
            List<Instance> instances = namingService.selectInstances(serverName, true);
            if (CollectionUtils.isEmpty(instances)) {
                log.warn("no instance in service {}", serverName);
                return null;
            }
            // 筛选指定版本 version为空 筛选version为空的实例
            List<Instance> metadataMatchInstances = screenVersion(instances, version);
            if (CollectionUtils.isEmpty(metadataMatchInstances)) {
                log.warn("未找到元数据匹配的目标实例！请检查配置。version = {}, instance = {}", version, instances);
                return null;
            }

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

            // 按nacos权重获取。这个是NacosRule的代码copy过来 没有自己实现权重随机。这个权重是nacos控制台服务的权重设置
            // 如果业务上有自己特殊的业务。可以自己定制规则，黑白名单，用户是否是灰度用户，测试账号。等等一些自定义设置
            Instance instance = ExtendBalancer.getHostByRandomWeight2(clusterMetadataMatchInstances);
            return new NacosServer(instance);
        } catch (Exception var9) {
            log.error("自定义网关灰度ribbon异常");
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

        // 进行按版本分组排序(version为空也可以)
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
