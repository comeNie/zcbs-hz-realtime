package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class OrgnlMsgIdBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8714150717385061799L;
	@JSONField(name="OrgnlSender")
	private String orgnlSender;
	@JSONField(name="OrgnlMsgId")
	private String orgnlMsgId;
	@JSONField(name="OrgnlMsgType")
	private String ognlMsgType;
	public String getOrgnlSender() {
		return orgnlSender;
	}
	public void setOrgnlSender(String orgnlSender) {
		this.orgnlSender = orgnlSender;
	}
	public String getOrgnlMsgId() {
		return orgnlMsgId;
	}
	public void setOrgnlMsgId(String orgnlMsgId) {
		this.orgnlMsgId = orgnlMsgId;
	}
	public String getOgnlMsgType() {
		return ognlMsgType;
	}
	public void setOgnlMsgType(String ognlMsgType) {
		this.ognlMsgType = ognlMsgType;
	}
	
	
}
