package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CMS911Bean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6415809246237727884L;
	@JSONField(name="MsgId")
	private String msgId;
	@JSONField(name="DscrdInf")
	private DiscardInformationBean dscrdInf;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public DiscardInformationBean getDscrdInf() {
		return dscrdInf;
	}
	public void setDscrdInf(DiscardInformationBean dscrdInf) {
		this.dscrdInf = dscrdInf;
	}
}
