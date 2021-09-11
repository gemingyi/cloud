package com.example.pluginnetty.webSocket.byteArray;

import com.example.pluginnetty.anno.EnableWebSocket;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("com.example.pluginnetty.*")
@SpringBootApplication
@EnableWebSocket
public class PluginNettyApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PluginNettyApplication.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
