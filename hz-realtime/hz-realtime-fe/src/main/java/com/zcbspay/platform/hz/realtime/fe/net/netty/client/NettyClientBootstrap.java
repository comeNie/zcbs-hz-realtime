package com.zcbspay.platform.hz.realtime.fe.net.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

import java.net.InetSocketAddress;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Socket短连接客户端<br>
 * 建立连接发送消息成功后，关闭客户端
 * 
 * @author AlanMa
 *
 */
public class NettyClientBootstrap {

    private static final Logger log = LoggerFactory.getLogger(NettyClientBootstrap.class);
    private int port;
    private String host;
    private SocketChannel socketChannel;

    public NettyClientBootstrap(int port, String host) throws InterruptedException {
        this.port = port;
        this.host = host;
        start();
    }

    public void start() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host, port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ByteArrayDecoder());
                socketChannel.pipeline().addLast(new ByteArrayEncoder());
                socketChannel.pipeline().addLast(new NettyClientHandler());
            }
        });
        ChannelFuture future = bootstrap.connect(host, port).sync();

        if (future.isSuccess()) {
            socketChannel = (SocketChannel) future.channel();
            InetSocketAddress localAddress = socketChannel.localAddress();
            log.info("本地{}:{}-->{}:{} 连接成功", new Object[] { localAddress.getAddress().getHostAddress(), localAddress.getPort(), host, port });
            SocketChannelHelper channelHelper = SocketChannelHelper.getInstance();
            channelHelper.setLastActiveTime(new Date());
            channelHelper.setSocketKey(localAddress.getAddress().getHostAddress() + ":" + localAddress.getPort());
        }
    }

    public void sendMessage(byte[] msg) throws InterruptedException {
        ChannelFuture channelFuture = socketChannel.writeAndFlush(msg).sync();
        if (channelFuture.isSuccess()) {
            shutdown();
        }
    }

    private void shutdown() {
        if (socketChannel != null) {
            socketChannel.close();
            socketChannel = null;
            log.info("本地[{}]关闭", SocketChannelHelper.getInstance().getSocketKey());
        }
    }
}
