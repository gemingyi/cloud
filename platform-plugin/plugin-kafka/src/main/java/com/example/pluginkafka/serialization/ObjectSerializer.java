package com.example.pluginkafka.serialization;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;


/**
 * 对象序列化
 * Create by IntelliJ Idea 2018.1
 * Company: silita
 * Author: gemingyi
 * Date: 2019-03-15 17:06
 */
public class ObjectSerializer implements Serializer<Object> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Object o) {
        return SerializationUtil.serialize(o);
    }

    @Override
    public void close() {

    }
}

