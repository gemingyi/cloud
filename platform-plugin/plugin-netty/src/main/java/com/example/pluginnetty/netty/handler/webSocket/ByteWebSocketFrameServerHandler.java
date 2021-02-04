package com.example.pluginnetty.netty.handler.webSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,  Object msg){
        //首次请求之后先进行握手，通过http请求来实现
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(channelHandlerContext, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketRequest(channelHandlerContext, (WebSocketFrame) msg);
        }

    }


    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //
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

        // 文本或者二进制
        byte[] contentBytes = new byte[frame.content().readableBytes()];
        frame.content().readBytes(contentBytes);
        ctx.writeAndFlush(contentBytes);
    }

    private boolean isWebSocketRequest(FullHttpRequest req) {
        return req != null
                && req.decoderResult().isSuccess()
                && "websocket".equals(req.headers().get("Upgrade"));
    }


}
