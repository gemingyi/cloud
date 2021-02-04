package com.example.pluginnetty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 16:06
 */
@Component
@ConfigurationProperties(prefix = "socket.config")
@Data
public class SocketConfiguration {

    /**
     * 端口号
     */
    private int port = 9090;

    /**
     * 协议格式
     */
    private String protocolType = "text";

    /**
     * 启动boss线程数
     */
    private int bossThreadCount = 1;

    /**
     * 启动work线程数
     */
    private int workThreadCount = 2;

    /**
     * 未检测到心跳挂断时间（秒）
     */
    private int heartTimeout = 5;

    /**
     * SSL相关配置
     */
//    SslInfo sslInfo = new SslInfo();

}
