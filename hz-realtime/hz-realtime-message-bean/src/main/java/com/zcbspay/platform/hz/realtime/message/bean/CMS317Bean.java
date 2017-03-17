package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CMS317Bean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7964617315028514455L;
	@JSONField(name="MsgId")
	private String msgId;
	@JSONField(name="OrgnlTx")
	private OrgnlTxBean orgnlTx;
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
	public OrgnlTxBean getOrgnlTx() {
		return orgnlTx;
	}
	public void setOrgnlTx(OrgnlTxBean orgnlTx) {
		this.orgnlTx = orgnlTx;
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
