package com.zcbspay.platform.hz.realtime.fe.net.netty.server;

import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zcbspay.platform.hz.realtime.fe.net.netty.server.impl.SocketServerHZImpl;

/**
 * 启动Server
 * 
 * @author AlanMa
 *
 */
@Configuration
public class ServerConfig {

    @Value("${socket.server.port}")
    private int port;

    @Value("${socket.server.charset}")
    private String chaset;

    @Bean(initMethod = "run", destroyMethod = "shutdown")
    public SocketServerHZImpl socketServer() {
        SocketServerHZImpl socketServer = new SocketServerHZImpl(port);
        socketServer.setBossGroup(new NioEventLoopGroup(2));
        socketServer.setWorkerGroup(new NioEventLoopGroup(10));
        socketServer.setChannelOptions(channelOptions());
        socketServer.setChannelInitalizer(channelInitalizerZCBS());
        return socketServer;
    }

    @Bean
    public Map<ChannelOption<?>, Object> channelOptions() {
        Map<ChannelOption<?>, Object> map = new HashMap<>();
        map.put(ChannelOption.SO_REUSEADDR, true);
        map.put(ChannelOption.SO_KEEPALIVE, true);
        map.put(ChannelOption.TCP_NODELAY, true);
        return map;
    }

    @Bean
    public ChrLengthFieldChannelInitializer channelInitalizerZCBS() {
        ChrLengthFieldChannelInitializer channelInitializer = new ChrLengthFieldChannelInitializer();
        channelInitializer.setTimeout(100);
        channelInitializer.setCharset(Charset.forName(chaset));
        channelInitializer.setFailFast(true);
        return channelInitializer;
    }

}
