package com.example.pluginnetty;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class PluginNettyApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PluginNettyApplication.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }


}
