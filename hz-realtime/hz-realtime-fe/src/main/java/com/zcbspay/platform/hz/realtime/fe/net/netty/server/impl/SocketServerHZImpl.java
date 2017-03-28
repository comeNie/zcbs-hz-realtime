package com.zcbspay.platform.hz.realtime.fe.net.netty.server.impl;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.fe.net.netty.server.SocketServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

@Service("socketServerHZ")
public class SocketServerHZImpl implements SocketServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServerHZImpl.class);

    private int port;

    private Date startTime;

    public ChannelGroup channelGroup;

    private ChannelInitializer<SocketChannel> channelInitalizer;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    private Map<ChannelOption<?>, Object> channelOptions;

    public SocketServerHZImpl() {
        super();
    }

    public SocketServerHZImpl(int port) {
        this.port = port;
        this.startTime = null;
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(this.bossGroup, this.workerGroup).channel(NioServerSocketChannel.class).childHandler(this.getChannelInitalizer());

        for (ChannelOption option : channelOptions.keySet()) {
            b.option(option, channelOptions.get(option));
        }
        return b;
    }

    @Override
    public void run() {
        try {
            this.startTime = new Date();

            ServerBootstrap bootstrap = bootstrap();
            ChannelFuture f = bootstrap.bind(port).sync();
            channelGroup.add(f.channel());
            logger.info("Bind() has complete for {}", this.port);
        }
        catch (Exception ee) {
            logger.error("Bind() error: {}", ee.getMessage());
            this.startTime = null;
        }
    }

    @Override
    public void shutdown() {
        if (channelGroup != null) {
            final ChannelGroupFuture f = channelGroup.close().awaitUninterruptibly();
            f.addListener(ChannelFutureListener.CLOSE);
            this.startTime = null;
        }
        logger.info("hzqszx socket server closed");
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setChannelInitalizer(ChannelInitializer<SocketChannel> channelInitalizer) {
        this.channelInitalizer = channelInitalizer;
    }

    public ChannelInitializer<SocketChannel> getChannelInitalizer() {
        return channelInitalizer;
    }

    public void setBossGroup(NioEventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    public void setWorkerGroup(NioEventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public int getPort() {
        return port;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setChannelOptions(Map<ChannelOption<?>, Object> channelOptions) {
        this.channelOptions = channelOptions;
    }

}
