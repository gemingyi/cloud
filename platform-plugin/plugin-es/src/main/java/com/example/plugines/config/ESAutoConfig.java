package com.example.plugines.config;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties({ElasticsearchProperties.class})
@ConditionalOnProperty(prefix = "spring.data.elasticsearch", name = {"cluster-nodes"})
public class ESAutoConfig {

    private final ElasticsearchProperties elasticsearchProperties;

    public ESAutoConfig(ElasticsearchProperties properties) {
        this.elasticsearchProperties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public TransportClient elasticsearchClient(ElasticsearchProperties properties) throws Exception {
        TransportClientFactoryBean factory = new TransportClientFactoryBean();
        factory.setClusterNodes(properties.getClusterNodes());
        factory.setProperties(createProperties());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    private Properties createProperties() {
        Properties properties = new Properties();
        properties.put("cluster.name", this.elasticsearchProperties.getClusterName());
        properties.putAll(this.elasticsearchProperties.getProperties());
        return properties;
    }

}
