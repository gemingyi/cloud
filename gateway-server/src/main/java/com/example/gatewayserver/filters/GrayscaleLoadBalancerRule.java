package com.example.gatewayserver.filters;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.Objects;
import com.alibaba.nacos.common.utils.StringUtils;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
* @Description: 网关灰度发布负载均衡算法
* @author mingyi ge
* @date 2020/12/10 11:57
* @params
*/
@Slf4j
public class GrayscaleLoadBalancerRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        // 读取配置文件，并初始化NacosWeightLoadBalancerRule
    }

    @Override
    public Server choose(Object key) {

        try {
            GatewayLoadBalancerClientFilter.GrayscaleProperties grayscale = (GatewayLoadBalancerClientFilter.GrayscaleProperties) key;
            String version = grayscale.getVersion();
            String clusterName = this.nacosDiscoveryProperties.getClusterName();
            NamingService namingService = this.nacosDiscoveryProperties.namingServiceInstance();
            List<Instance> instances = namingService.selectInstances(grayscale.getServerName(), true);

            if (CollectionUtils.isEmpty(instances)) {
                log.warn("no instance in service {}", grayscale.getServerName());
                return null;
            } else {
                List<Instance> instancesToChoose = buildVersion(instances,version);
                //进行cluster-name分组筛选
                // TODO 思考如果cluster-name 节点全部挂掉。是不是可以请求其他的分组的服务？可以根据情况在定制一份规则出来
                if (StringUtils.isNotBlank(clusterName)) {
                    List<Instance> sameClusterInstances = instancesToChoose.stream().filter((instancex) -> {
                        return Objects.equals(clusterName, instancex.getClusterName());
                    }).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(sameClusterInstances)) {
                        instancesToChoose = sameClusterInstances;
                    } else {
                        log.warn("A cross-cluster call occurs，name = {}, clusterName = {}, instance = {}", new Object[]{grayscale.getServerName(), clusterName, instances});
                    }
                }
                //按nacos权重获取。这个是NacosRule的代码copy 过来 没有自己实现权重随机。这个权重是nacos控制台服务的权重设置
                // 如果业务上有自己特殊的业务。可以自己定制规则，黑白名单，用户是否是灰度用户，测试账号。等等一些自定义设置
                Instance instance = ExtendBalancer.getHostByRandomWeight2(instancesToChoose);
                return new NacosServer(instance);
            }
        } catch (Exception var9) {
            log.warn("NacosRule error", var9);
            return null;
        }
    }

    /**
     * 筛选想要的值
     * @param instances
     * @param version
     * @return
     */
    protected List <Instance> buildVersion(List<Instance> instances,String version){
        //进行按版本分组排序
        Map<String,List<Instance>> versionMap = getInstanceByScreen(instances);
        if(versionMap.isEmpty()){
            log.warn("no instance in service {}", version);
        }
        //如果version 未传值使用最低版本服务
        if(StringUtils.isBlank( version )){
            version = getFirst( versionMap.keySet() );
        }

        List <Instance> instanceList = versionMap.get( version );

        return instanceList;
    }

    /**
     * 根据version 组装一个map key value  对应 version List<Instance>
     * @param instances
     * @return
     */
    protected Map<String,List<Instance>> getInstanceByScreen(List<Instance> instances){

        Map<String,List<Instance>> versionMap = new HashMap<>( instances.size() );
        instances.stream().forEach( instance -> {
            String version = instance.getMetadata().get("api-version");
            List <Instance> versions = versionMap.get( version );
            if(versions == null){
                versions = new ArrayList<>(  );
            }
            versions.add( instance );
            versionMap.put( version,versions );
        } );
        return versionMap;
    }

    /**
     * 获取第一个值
     * @param keys
     * @return
     */
    protected String getFirst(Set<String> keys){
        List <String> list = sortVersion( keys );
        return list.get( 0 );
    }

    /**
     * 获取最后一个值
     * @param keys
     * @return
     */
    protected String getLast(Set <String> keys){
        List <String> list = sortVersion( keys );
        return list.get( list.size()-1 );
    }

    /**
     * 根据版本排序
     * @param keys
     * @return
     */
    protected List<String > sortVersion(Set <String> keys){
        List<String > list = new ArrayList <>( keys );
        Collections.sort(list);
        return list;
    }

}
