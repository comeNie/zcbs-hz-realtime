package com.zcbspay.platform.hz.realtime.fe.net.netty.client.sync;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty线程阻塞客户端
 * 
 * @author AlanMa
 *
 */
public class NettySyncClient {

    private static final Logger logger = LoggerFactory.getLogger(NettySyncClient.class);
    /**
     * 发送的消息
     */
    byte[] message = null;
    /**
     * 应答消息
     */
    StringBuffer respMessage = new StringBuffer();

    /**
     * 发送消息（同步返回结果）
     * 
     * @param host
     * @param port
     * @return
     * @throws Exception
     */
    public String sendMessage(String host, int port, byte[] messageToSend) throws Exception {
        message = messageToSend;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new NettyClientSyncHandler(message, respMessage));
                }
            });
            b.connect(host, port).channel().closeFuture().await();
            logger.info("【NettySyncClient.sendMessage respMessage is】:" + respMessage.toString());
            return respMessage.toString();
        }
        finally {
            workerGroup.shutdownGracefully();
        }
    }
}
