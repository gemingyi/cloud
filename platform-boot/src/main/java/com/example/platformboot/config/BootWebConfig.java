package com.example.platformboot.config;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * https://www.cnblogs.com/crazy-lc/p/12312715.html 处理JSON
 * https://www.jianshu.com/p/becf73e7b06e   WebMvcConfigurationSupport 详解
 */
@Configuration
public class BootWebConfig extends WebMvcConfigurationSupport {


//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = new ObjectMapper();
//        /**
//         * 序列换成json时,将全部的long变成string
//         * 由于js中得数字类型不能包含全部的java long值
//         */
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(Long.class, new CustomLongSerializer());
//        simpleModule.addSerializer(Long.TYPE, new CustomLongSerializer());
//        objectMapper.registerModule(simpleModule);
//        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
//        converters.add(jackson2HttpMessageConverter);
//    }


    /**
     * 使用阿里 fastjson 作为 JSON MessageConverter
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                // 排序配置
                SerializerFeature.SortField,
                SerializerFeature.MapSortField,
                // 保留 Map 空的字段
                SerializerFeature.WriteMapNullValue,
                // 将 String 类型的 null 转成 ""
//                SerializerFeature.WriteNullStringAsEmpty,
                // 将 Number 类型的 null 转成 0
//                SerializerFeature.WriteNullNumberAsZero,
                // 将 List 类型的 null 转成 []
//                SerializerFeature.WriteNullListAsEmpty,
                // 将 Boolean 类型的 null 转成 false
//                SerializerFeature.WriteNullBooleanAsFalse,
                // 时间
                SerializerFeature.WriteDateUseDateFormat,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect,
                // 忽略配置
                SerializerFeature.IgnoreErrorGetter,
                SerializerFeature.IgnoreNonFieldGetter,
                // 格式化
                SerializerFeature.PrettyFormat);
        // 让json里的属性值不排序
        config.setFeatures(Feature.OrderedField);

        // long 序列化
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(Long.class, LongToStringSerializer.INSTANCE);
        serializeConfig.put(Long.TYPE, LongToStringSerializer.INSTANCE);
        config.setSerializeConfig(serializeConfig);

        converter.setFastJsonConfig(config);

        //
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
//        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
//        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
//        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
//        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
//        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
//        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
//        supportedMediaTypes.add(MediaType.APPLICATION_XML);
//        supportedMediaTypes.add(MediaType.IMAGE_GIF);
//        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
//        supportedMediaTypes.add(MediaType.IMAGE_PNG);
//        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
//        supportedMediaTypes.add(MediaType.TEXT_HTML);
//        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
//        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
//        supportedMediaTypes.add(MediaType.TEXT_XML);
        converter.setSupportedMediaTypes(supportedMediaTypes);

        //
        converter.setDefaultCharset(StandardCharsets.UTF_8);

        converters.add(converter);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath*:/META-INF/resources/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath*:/META-INF/resources/");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath*:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath*:/META-INF/resources/webjars/");
    }
}


/**
 * long 序列化
 */
class LongToStringSerializer implements ObjectSerializer {
    public static final LongToStringSerializer INSTANCE = new LongToStringSerializer();
    private final int JS_MAX_VALUE_LENGTH = 17;

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        // null
        if (object == null) {
            out.writeNull();
            return;
        }
        // 大于17 js丢失精度
        if (object.toString().length() >= JS_MAX_VALUE_LENGTH) {
            String strVal = object.toString();
            out.writeString(strVal);
            return;
        }
        //
        long value = (Long) object;
        out.writeLong(value);
    }
}


//class CustomLongSerializer extends JsonSerializer<Long> {
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