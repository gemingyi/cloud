//package com.example.platformboot.config;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//
//import java.io.IOException;
//
///**
// * https://www.cnblogs.com/crazy-lc/p/12312715.html
// * @description:
// * @author: mingyi ge
// * @date: 2021/11/1 18:25
// */
//@Configuration
//public class JacksonConfig {
//
//    @Bean("jackson2ObjectMapperBuilderCustomizer")
//    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
//        return new Jackson2ObjectMapperBuilderCustomizer() {
//            @Override
//            public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
//                jacksonObjectMapperBuilder.serializerByType(Long.class,new CustomLongSerialize());
//                jacksonObjectMapperBuilder.serializerByType(Long.TYPE, new CustomLongSerialize());
//            }
//        };
//    }
//
//}
//
//
//
//class CustomLongSerialize extends JsonSerializer<Long> {
//
//
//    /**
//     * Method that can be called to ask implementation to serialize
//     * values of type this serializer handles.
//     *
//     * @param value       Value to serialize; can <b>not</b> be null.
//     * @param gen         Generator used to output resulting Json content
//     * @param serializers Provider that can be used to get serializers for
//     */
//    @Override
//    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//        if (value != null && value.toString().length() > 16) {
//            gen.writeString(value.toString());
//        } else {
//            gen.writeNumber(value);
//        }
//    }
//}
