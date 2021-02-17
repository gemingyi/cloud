package com.example.pluginnetty.netty.adapter;

import io.netty.channel.ChannelPipeline;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 11:03
 */
public interface ChannelPipelineAdapter {

    void adapterChannelPipeline(ChannelPipeline pipeline);

}
