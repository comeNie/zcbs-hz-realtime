package com.zcbspay.platform.hz.realtime.message.common;


public interface MessageBean {
	
	/**
	 * 获取报文的类型返回的是枚举
	 * @return
	 */
	public MessageTypeEnum getBeanType();
	/**
	 * 返回Object类型的messagebean，此MessageBean为具体人行报文体bean，通过getBeanType()方法进行类型转换
	 * @return
	 */
	public Object getCNAPSMessageBean();
}
