package com.example.gatewayserver.swagger;


import com.sun.org.apache.xerces.internal.parsers.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.List;

@RestController
@RequestMapping("/swagger-resources")
public class SwaggerResourceController {

    private SwaggerResourceProvider swaggerProvider;

    @Autowired
    public SwaggerResourceController(SwaggerResourceProvider swaggerProvider) {
        this.swaggerProvider = swaggerProvider;
    }

    @RequestMapping(value = "/configuration/security")
    public ResponseEntity<SecurityConfiguration> securityConfiguration() {
        return new ResponseEntity(SecurityConfigurationBuilder.builder().build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/configuration/ui")
    public ResponseEntity<UiConfiguration> uiConfiguration() {
        return new ResponseEntity<>(UiConfigurationBuilder.builder().build(), HttpStatus.OK);
    }

    @RequestMapping
    public ResponseEntity<List<SwaggerResource>> swaggerResources() {
        return new ResponseEntity<>(swaggerProvider.get(), HttpStatus.OK);
    }
}
