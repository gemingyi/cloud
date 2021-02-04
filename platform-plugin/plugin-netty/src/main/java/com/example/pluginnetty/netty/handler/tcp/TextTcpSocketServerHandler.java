package com.example.pluginnetty.netty.handler.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 11:45
 */
public class TextTcpSocketServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        String text = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("收到消息" + text);
        channelHandlerContext.writeAndFlush(text);
//        doHandler(() -> socketMsgHandler.onMessage(ctx, text), ctx);
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
