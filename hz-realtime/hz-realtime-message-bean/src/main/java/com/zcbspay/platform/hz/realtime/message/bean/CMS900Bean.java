package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CMS900Bean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5510484292610992812L;
	@JSONField(name="MsgId")
	private String msgId;
	@JSONField(name="OrgnlMsgId")
	private OrgnlMsgIdBean orgnlMsgId;
	@JSONField(name="RspnInf")
	private RspnInfBean rspnInf;
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public OrgnlMsgIdBean getOrgnlMsgId() {
		return orgnlMsgId;
	}
	public void setOrgnlMsgId(OrgnlMsgIdBean orgnlMsgId) {
		this.orgnlMsgId = orgnlMsgId;
	}
	public RspnInfBean getRspnInf() {
		return rspnInf;
	}
	public void setRspnInf(RspnInfBean rspnInf) {
		this.rspnInf = rspnInf;
	}
	
	
}
