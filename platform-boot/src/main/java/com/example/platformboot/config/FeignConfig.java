package com.example.platformboot.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.ContentType;
import feign.form.FormEncoder;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringManyMultipartFilesWriter;
import feign.form.spring.SpringSingleMultipartFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
* @Description feign
* @Author mingyi ge
* @Date 2020/9/9 20:36
*/
@Configuration
public class FeignConfig {

    @Bean
    public Encoder feignFormEncoder() {
        return new FeignSpringFormEncoder();
    }


    class FeignSpringFormEncoder extends FormEncoder {
        /**
         * Constructor with the default Feign's encoder as a delegate.
         */
        public FeignSpringFormEncoder() {
            this(new Default());
        }
        /**
         * Constructor with specified delegate encoder.
         *
         * @param delegate delegate encoder, if this encoder couldn't encode object.
         */
        public FeignSpringFormEncoder(Encoder delegate) {
            super(delegate);

            MultipartFormContentProcessor processor = (MultipartFormContentProcessor) getContentProcessor(ContentType.MULTIPART);
            processor.addWriter(new SpringSingleMultipartFileWriter());
            processor.addWriter(new SpringManyMultipartFilesWriter());
        }

        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            if (bodyType.equals(MultipartFile.class)) {
                MultipartFile file = (MultipartFile) object;
                Map data = Collections.singletonMap(file.getName(), object);
                super.encode(data, MAP_STRING_WILDCARD, template);
                return;
            } else if (bodyType.equals(MultipartFile[].class)) {
                MultipartFile[] file = (MultipartFile[]) object;
                if (file != null) {
                    Map data = Collections.singletonMap(file.length == 0 ? "" : file[0].getName(), object);
                    super.encode(data, MAP_STRING_WILDCARD, template);
                    return;
                }
            }
            super.encode(object, bodyType, template);
        }
    }

    /**
    * @Description feign向下游服务传递 请求头
    * @Author mingyi ge
    * @Date 2020/9/13 14:20
    */
    @Bean
    public RequestInterceptor headerInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        template.header(name, values);
                    }
                }
            }
        };
    }



}
