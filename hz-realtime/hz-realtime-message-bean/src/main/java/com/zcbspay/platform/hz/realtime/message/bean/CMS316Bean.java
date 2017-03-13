package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CMS316Bean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6166658216950321730L;
	@JSONField(name="MsgId")
	private String msgId;
	@JSONField(name="OrgnlTx")
	private OrgnlTxBean orgnlTx;
	
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public OrgnlTxBean getOrgnlTx() {
		return orgnlTx;
	}
	public void setOrgnlTx(OrgnlTxBean orgnlTx) {
		this.orgnlTx = orgnlTx;
	}
	
	
}
