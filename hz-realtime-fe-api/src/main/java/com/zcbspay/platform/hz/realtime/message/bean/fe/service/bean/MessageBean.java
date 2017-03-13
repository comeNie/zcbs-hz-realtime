package com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean;

public class MessageBean {

	/**
	 * 要发送报文
	 */
	private String sendMsg;
	/**
	 * 报文类型
	 */
	private MessageTypeEnum messageType;
	
	public String getSendMsg() {
		return sendMsg;
	}
	public void setSendMsg(String sendMsg) {
		this.sendMsg = sendMsg;
	}
	public MessageTypeEnum getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageTypeEnum messageType) {
		this.messageType = messageType;
	}
	
	
}
