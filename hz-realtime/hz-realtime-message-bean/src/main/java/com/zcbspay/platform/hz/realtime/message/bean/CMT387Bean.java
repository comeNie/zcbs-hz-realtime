package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CMT387Bean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7151088107438479903L;
	@JSONField(name="MsgId")
	private String msgId;
	@JSONField(name="OrgnlMsgId")
	private OrgnlMsgIdBean orgnlMsgId;
	@JSONField(name="RspnInf")
	private RspnInfBean rspnInf;
	@JSONField(name="BusiText")
	private BusiTextBean busiText;
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
	public BusiTextBean getBusiText() {
		return busiText;
	}
	public void setBusiText(BusiTextBean busiText) {
		this.busiText = busiText;
	}
	
	
}
