package com.example.pluginnetty.anno;

import com.example.pluginnetty.config.SocketConfiguration;
import com.example.pluginnetty.config.WebSocketChannelAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 21:54
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SocketConfiguration.class, WebSocketChannelAutoConfig.class})
public @interface EnableWebSocket {
}
