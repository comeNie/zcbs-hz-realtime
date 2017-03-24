package com.zcbspay.platform.hz.realtime.fe.net.netty.server;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 渠道参数配置
 * @author AlanMa
 *
 */
public abstract class AbstractChannelInitializer extends ChannelInitializer<SocketChannel> {

	protected int timeout;

	protected String charsetName;

	protected boolean failFast;

	protected Charset charset;

	protected ByteOrder byteOrder;

	public AbstractChannelInitializer() {
		this.timeout = 120;
		this.charset = Charset.forName("GBK");
		this.failFast = true;
		this.byteOrder = ByteOrder.LITTLE_ENDIAN;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	public boolean isFailFast() {
		return failFast;
	}

	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	public void setByteOrder(ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}
}
