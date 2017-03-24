package com.zcbspay.platform.hz.realtime.fe.net.netty.server;

/**
 * SocketServer管理
 * @author AlanMa
 *
 */
public interface SocketServer {

	/**
	 * 启动Socket Server
	 */
	public void run();

	/**
	 * 关闭Socket Server
	 */
	public void shutdown();
}
