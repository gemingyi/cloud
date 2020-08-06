package com.example.pluginkafka.config;

import com.example.pluginkafka.serialization.ObjectDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * https://www.cnblogs.com/EnzoDin/p/12641719.html
 */
@Configuration
@EnableKafka
@EnableConfigurationProperties({KafkaProperties.class})
@ConditionalOnProperty(name = {"spring.kafka.bootstrap-servers"})
public class KafkaConsumerAutoConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConsumerAutoConfig(KafkaProperties properties) {
        this.kafkaProperties = properties;
    }


    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        //是否自动提交偏移量，默认值是true,为了避免出现重复数据和数据丢失，可以把它设置为false,然后手动提交偏移量
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        //consumer向zookeeper提交offset的时间间隔, 自动提交时需要设置这个
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaProperties.getConsumer().getAutoCommitInterval());
        //zookeeper 会话的超时限制。如果consumer在这段时间内没有向zookeeper发送心跳信息，则它会被认为挂掉了，
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "8000");
        //*** 一次从kafka中poll出来的数据条数 ***
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        //*** 消费者处理逻辑最大时间 max.poll.records条数据需要在max.poll.interval.ms这个时间内处理完 ***
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "100000");
        //当zookeeper中没有初始的offset时候的处理方式 。smallest ：重置为最小值 largest:重置为最大值 anythingelse：抛出异常
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ObjectDeserializer.class);
        return props;
    }


    /**
     * 单线程-单条消费
     */
    @Bean
    public KafkaListenerContainerFactory kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));
        return factory;
    }

}
