package com.example.pluginnetty.netty.handler.webSocket;

import com.example.pluginnetty.config.SocketConfiguration;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import java.util.Map;


/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 15:05
 */
public class TextWebSocketFrameServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;


    SocketConfiguration socketConfiguration;

    public TextWebSocketFrameServerHandler(SocketConfiguration socketConfiguration) {
        this.socketConfiguration = socketConfiguration;
    }
    

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
        socketConfiguration.getChannelMap().remove(ctx.channel().id());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) {
        //解码http失败返回
        if (!req.decoderResult().isSuccess()) {
            sendResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.BAD_REQUEST, ctx.alloc().buffer()));
            return;
        }

        //
        if (!HttpMethod.GET.equals(req.method())) {
            sendResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.FORBIDDEN, ctx.alloc().buffer()));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(req.uri(), null, true);
        this.handshaker = wsFactory.newHandshaker(req);
        if (this.handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            this.handshaker.handshake(ctx.channel(), req);
            // 打印连接上
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

        if (frame instanceof TextWebSocketFrame) {
            // 文本或者二进制
            String text = ((TextWebSocketFrame) frame).text();
            System.out.println("收到消息" + text);
//            ctx.channel().writeAndFlush(text);
//            ctx.writeAndFlush(text);

            ChannelId id = ctx.channel().id();
            System.out.println(id);
            Map<ChannelId, Channel> map = socketConfiguration.getChannelMap();
            map.forEach((k,v) -> {
                System.out.println("current" + ctx.channel().id() + ", map" + v.id());
//                if(id.toString().equals(k)){
//                    return;
//                }
                v.writeAndFlush(text);
            });
        }

    }


    private void sendResponse(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse resp) {
        HttpResponseStatus status = resp.status();
        if (status != HttpResponseStatus.OK) {
            ByteBufUtil.writeUtf8(resp.content(), status.toString());
            HttpUtil.setContentLength(req, resp.content().readableBytes());
        }
        boolean keepAlive = HttpUtil.isKeepAlive(req) && status == HttpResponseStatus.OK;
        HttpUtil.setKeepAlive(req, keepAlive);
        ChannelFuture future = ctx.write(resp);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

}
