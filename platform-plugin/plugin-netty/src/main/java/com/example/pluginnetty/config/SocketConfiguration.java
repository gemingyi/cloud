package com.example.pluginnetty.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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


    //连接id与容器的关系
    private Map<ChannelId, Channel> channelMap = new ConcurrentHashMap<>();

}
