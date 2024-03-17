package com.example.pluginnetty.netty.adapter;

import com.example.pluginnetty.config.SocketConfiguration;
import com.example.pluginnetty.netty.handler.ServiceWriteChannelHandler;
import com.example.pluginnetty.netty.handler.webSocket.ByteWebSocketFrameServerHandler;
//import com.example.pluginnetty.netty.handler.webSocket.TextWebSocketFrameServerHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 11:07
 */
public class WebSocketChannelPipelineAdapter implements ChannelPipelineAdapter {

    @Autowired
    private SocketConfiguration socketConfiguration;


    @Override
    public void adapterChannelPipeline(ChannelPipeline pipeline) {
        // 因为基于http协议，使用http的编码和解码器
        pipeline.addLast(new HttpServerCodec());
        //http是以块方式写，添加ChunkedWriteHandler处理器
        pipeline.addLast(new ChunkedWriteHandler());
        /*
         * 1. http数据在传输过程中是分段, HttpObjectAggregator ，就是可以将多个段聚合
         * 2. 这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
         */
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(new HttpServerExpectContinueHandler());
       /*
        * 1. 对应websocket ，它的数据是以 帧(frame) 形式传递
        * 2. 可以看到WebSocketFrame 下面有六个子类
        * 3. 浏览器请求时 ws://localhost:7000/hello 表示请求的uri
        * 4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
        * 5. 是通过一个 状态码 101
         */
//        pipeline .addLast(new WebSocketServerProtocolHandler("/", null, true, 10485760));
        pipeline.addLast(new ServiceWriteChannelHandler());
//        if ("text".equals(socketConfiguration.getProtocolType())) {
//            pipeline.addLast(new TextWebSocketFrameServerHandler(socketConfiguration));
//        } else {
            pipeline.addLast(new ByteWebSocketFrameServerHandler());
//        }
    }

}
