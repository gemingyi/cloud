package com.example.pluginnetty.netty.handler.webSocket;

import com.example.pluginnetty.util.Person;
import com.example.pluginnetty.util.SerializationUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;


/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 15:05
 */
public class ByteWebSocketFrameServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) {
        //首次请求之后先进行握手，通过http请求来实现
        if (msg instanceof HttpRequest) {
            handleHttpRequest(channelHandlerContext, (HttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketRequest(channelHandlerContext, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("连接断开=" + ctx.channel().toString());
        ctx.fireChannelInactive();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) {
        if (isWebSocketRequest(req)) {
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(req.uri(), null, true);
            this.handshaker = wsFactory.newHandshaker(req);
            if (this.handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                this.handshaker.handshake(ctx.channel(), req);
                // 打印连接上
            }
        }
    }

    private void handleWebSocketRequest(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 关闭
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        // 握手 PING/PONG
        if (frame instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (frame instanceof BinaryWebSocketFrame) {
            // 文本或者二进制
            byte[] contentBytes = new byte[frame.content().readableBytes()];
//            ByteBuf byteBuf = frame.content().readBytes(contentBytes);
            System.out.println("server byte message:"+ SerializationUtil.deserializer(contentBytes, Person.class));
            ctx.writeAndFlush(contentBytes);
        }
    }

    private boolean isWebSocketRequest(HttpRequest req) {
        return req != null
                && req.decoderResult().isSuccess()
                && "websocket".equals(req.headers().get("Upgrade"));
    }


}
