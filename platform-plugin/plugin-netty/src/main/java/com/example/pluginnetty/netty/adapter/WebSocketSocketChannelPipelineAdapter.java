package com.example.pluginnetty.netty.adapter;

import com.example.pluginnetty.config.SocketConfiguration;
import com.example.pluginnetty.netty.handler.webSocket.ByteWebSocketFrameServerHandler;
import com.example.pluginnetty.netty.handler.webSocket.TextWebSocketFrameServerHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 11:07
 */
@Component
public class WebSocketSocketChannelPipelineAdapter implements ChannelPipelineAdapter {

    @Autowired
    private SocketConfiguration socketConfiguration;

    @Override
    public void adapterChannelPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpServerExpectContinueHandler());
        pipeline.addLast(new WriteChannelOutboundHandler());
        if("text".equals(socketConfiguration.getProtocolType())){
            pipeline.addLast(new TextWebSocketFrameServerHandler());
        }else{
            pipeline.addLast(new ByteWebSocketFrameServerHandler());
        }
    }

}
