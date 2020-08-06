package com.example.pluginkafka.serialization;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * 对象反序列化
 * Create by IntelliJ Idea 2018.1
 * Company: silita
 * Author: gemingyi
 * Date: 2019-03-15 17:08
 */
public class ObjectDeserializer implements Deserializer<Object> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public Object deserialize(String s, byte[] bytes) {
        return SerializationUtil.deserialize(bytes);
    }

    @Override
    public void close() {

    }
}
