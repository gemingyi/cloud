package com.example.pluginnetty.netty;


import com.example.pluginnetty.config.SocketConfiguration;
import com.example.pluginnetty.config.SslInfo;
import com.example.pluginnetty.netty.adapter.ChannelPipelineAdapter;
import com.example.pluginnetty.netty.handler.HeartBeatServerHandler;
import com.example.pluginnetty.netty.handler.SslFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/3 21:48
 */
@Slf4j
@Component
public class NettyServer implements ApplicationRunner, DisposableBean {

    @Autowired
    private SocketConfiguration socketConfiguration;
    @Autowired
    private ChannelPipelineAdapter channelPipelineAdapter;

    EventLoopGroup bossGroup = null;
    EventLoopGroup workerGroup = null;


    @Override
    public void run(ApplicationArguments args) {
        new Thread(this::start).start();
    }

    @Override
    public void destroy() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


    public void start() {
        SslInfo sslInfo = socketConfiguration.getSslInfo();

        ServerBootstrap bootstrap = new ServerBootstrap();

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            bossGroup = new NioEventLoopGroup(socketConfiguration.getBossThreadCount(), new DefaultThreadFactory("boss-group", true));
            workerGroup = new NioEventLoopGroup(socketConfiguration.getWorkThreadCount(), new DefaultThreadFactory("worker-group", true));
            bootstrap.channel(NioServerSocketChannel.class)
                    .group(bossGroup, workerGroup);
        } else {
            bossGroup = new EpollEventLoopGroup(socketConfiguration.getBossThreadCount(), new DefaultThreadFactory("boss-group", true));
            workerGroup = new EpollEventLoopGroup(socketConfiguration.getWorkThreadCount(), new DefaultThreadFactory("worker-group", true));
            bootstrap.channel(EpollServerSocketChannel.class)
                    .group(bossGroup, workerGroup).option(EpollChannelOption.TCP_CORK, true);
        }

        try {
            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) {
                    ChannelPipeline pipeline = channel.pipeline();

                    //
//                    socketConfiguration.getChannelMap().put(channel.id(), channel);
//                    log.info("new channel{}", channel);

//                    channel.closeFuture().addListener((ChannelFutureListener) future -> {
//                        log.info("channel close future {}", channel);
//                        socketConfiguration.getChannelMap().remove(future.channel().id());
//                    });

                    // 开启SSL验证
//                    pipeline.addLast("ssl", SslFactory.createSslContext(sslInfo.getCertFilePath(), sslInfo.getKeyFilePath()).newHandler(channel.alloc()));

                    // 设置N秒没有读到数据，则触发一个READER_IDLE事件。
                    pipeline.addLast(new IdleStateHandler(socketConfiguration.getHeartTimeout(), 0, 0, TimeUnit.SECONDS));

                    // 选择服务启动
                    channelPipelineAdapter.adapterChannelPipeline(pipeline);
                    pipeline.addLast(new HeartBeatServerHandler());
                }
            });

            //
            ChannelFuture future = bootstrap.bind(socketConfiguration.getPort()).sync();
            log.info("----------成功启动netty服务 端口[{}]----------", socketConfiguration.getPort());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
