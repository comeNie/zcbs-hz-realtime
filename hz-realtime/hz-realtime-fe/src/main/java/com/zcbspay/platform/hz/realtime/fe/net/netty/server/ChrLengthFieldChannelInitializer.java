/**
 * 
 */
package com.zcbspay.platform.hz.realtime.fe.net.netty.server;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;


/**
 * 渠道初始化
 * @author AlanMa
 *
 */
public class ChrLengthFieldChannelInitializer extends AbstractChannelInitializer {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new LoggingHandler());
		ch.pipeline().addLast(new ReadTimeoutHandler(this.timeout));
		ch.pipeline().addLast(new WriteTimeoutHandler(0));
		ch.pipeline().addLast(new StringEncoder(charset));
		ch.pipeline().addLast(new TcpServerHandler());
	}

}
