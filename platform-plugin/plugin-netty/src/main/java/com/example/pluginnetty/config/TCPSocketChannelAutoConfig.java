package com.example.pluginnetty.config;

import com.example.pluginnetty.netty.adapter.ChannelPipelineAdapter;
import com.example.pluginnetty.netty.adapter.TCPSocketChannelPipelineAdapter;
import org.springframework.context.annotation.Bean;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 22:06
 */
public class TCPSocketChannelAutoConfig {

    @Bean("channelPipelineAdapter")
    public ChannelPipelineAdapter tcpSocketChannelPipelineAdapter() {
        return new TCPSocketChannelPipelineAdapter();
    }

}
