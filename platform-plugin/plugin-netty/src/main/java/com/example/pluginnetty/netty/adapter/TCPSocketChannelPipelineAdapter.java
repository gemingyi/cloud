package com.example.pluginnetty.netty.adapter;

import com.example.pluginnetty.netty.handler.tcp.ByteTcpSocketServerHandler;
import com.example.pluginnetty.netty.handler.tcp.TextTcpSocketServerHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 11:06
 */
@Component
public class TCPSocketChannelPipelineAdapter implements ChannelPipelineAdapter {

    @Override
    public void adapterChannelPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("lengthEncode", new LengthFieldPrepender(4, false));
        pipeline.addLast("lengthDecoder", new LengthFieldBasedFrameDecoder(2000, 0, 4, 0, 4));
        pipeline.addLast(new WriteChannelOutboundHandler());
//        if("text".equals(socketConfiguration.getProtocolType())){
            pipeline.addLast(new TextTcpSocketServerHandler());
//        }else{
            pipeline.addLast(new ByteTcpSocketServerHandler());
//        }
    }

}
