package com.example.pluginnetty.config;

import com.example.pluginnetty.netty.adapter.ChannelPipelineAdapter;
import com.example.pluginnetty.netty.adapter.WebSocketChannelPipelineAdapter;
import org.springframework.context.annotation.Bean;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 22:07
 */
public class WebSocketChannelAutoConfig {

    @Bean("channelPipelineAdapter")
    public ChannelPipelineAdapter webSocketChannelPipelineAdapter() {
        return new WebSocketChannelPipelineAdapter();
    }

}
