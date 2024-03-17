package com.example.pluginnetty.netty.handler.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 12:34
 */
public class ByteTcpSocketServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        byte[] contentBytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(contentBytes);
        System.out.println("收到消息"+ new String(contentBytes, CharsetUtil.UTF_8));
        channelHandlerContext.writeAndFlush(contentBytes);
//        doHandler(() -> socketMsgHandler.onMessage(ctx, contentBytes), ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("断开连接=" + ctx.channel().toString());
    }

}
