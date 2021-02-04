package com.example.pluginnetty.netty.handler.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 10:33
 */
public class HttpHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private WebSocketServerHandshaker handshaker;


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) {
//        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
//                HttpResponseStatus.OK, Unpooled.wrappedBuffer("hello netty".getBytes()));
//
//        HttpHeaders heads = response.headers();
//        heads.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + "; charset=UTF-8");
//        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes()); // 3
//        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//
//        channelHandlerContext.writeAndFlush(response);

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(httpRequest.uri(), null, true);
        this.handshaker = wsFactory.newHandshaker(httpRequest);
        if (this.handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channelHandlerContext.channel());
        } else {
            this.handshaker.handshake(channelHandlerContext.channel(), httpRequest);

            //

        }

    }

}
