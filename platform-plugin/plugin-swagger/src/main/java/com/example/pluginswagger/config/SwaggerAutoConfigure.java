package com.example.pluginswagger.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerAutoConfigure {

    @Value("${spring.application.name}")
    private String applicationName;


    /**
     * swagger配置
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        System.out.println("###### 已加载Swagger插件 ######");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.*.controller"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(String.format("[%s] 微服务Api文档", applicationName)).version("1.0.0")
                .description(String.format("[%s] 微服务Api文档", applicationName))
                .contact(new Contact("gmy", "", ""))
                .build();
    }

}
