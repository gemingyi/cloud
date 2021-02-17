package com.example.pluginnetty.netty.handler;

import com.example.pluginnetty.netty.adapter.TCPSocketChannelPipelineAdapter;
import com.example.pluginnetty.util.SpringApplicationContextHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 14:23
 */
public class ServiceWriteChannelHandler extends ChannelOutboundHandlerAdapter {

    private static final String SOCKET_HANDLER_BUILDER_NAME = "channelPipelineAdapter";

    public final boolean isTcp = SpringApplicationContextHolder.getBean(SOCKET_HANDLER_BUILDER_NAME) instanceof TCPSocketChannelPipelineAdapter;


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (isTcp) {
            tcpWrite(ctx, msg, promise);
        } else {
            wsWrite(ctx, msg, promise);
        }
    }

    private void tcpWrite(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        byte[] contentBytes;
        if(msg instanceof String){
            msg = ((String) msg).getBytes();
        }
        if (msg instanceof byte[]) {
            contentBytes = (byte[]) msg;
            ctx.write(Unpooled.wrappedBuffer(contentBytes), promise);
        }
    }

    private void wsWrite(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if(msg instanceof String){
            ctx.write(new TextWebSocketFrame((String) msg), promise);
            return;
        }
        if (msg instanceof byte[]) {
            ctx.write(new BinaryWebSocketFrame(Unpooled.wrappedBuffer((byte[]) msg)), promise);
            return;
        }
        ctx.write(msg, promise);
    }

}
