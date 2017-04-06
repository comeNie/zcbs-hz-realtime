/**
 * 
 */
package com.zcbspay.platform.hz.realtime.fe.net.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcbspay.platform.hz.realtime.fe.net.netty.client.NettyClientHandler;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

/**
 * 渠道初始化
 * 
 * @author AlanMa
 *
 */
public class ChrLengthFieldChannelInitializer extends AbstractChannelInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ChrLengthFieldChannelInitializer.class);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("【SOCKET SERVER initChannel...】");
        ch.pipeline().addLast(new LoggingHandler());
        ch.pipeline().addLast(new ReadTimeoutHandler(this.timeout));
        ch.pipeline().addLast(new WriteTimeoutHandler(0));
        // -- 接收byteBuf--
        // ch.pipeline().addLast(new StringEncoder(charset));
        // -- 接收ByteBuf--
        // -- 接收byte[]--
        ch.pipeline().addLast(new ByteArrayDecoder());
        ch.pipeline().addLast(new ByteArrayEncoder());
        // -- 接收byte[]--
        ch.pipeline().addLast(new TcpServerHandler());
    }

}
