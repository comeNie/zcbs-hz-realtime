package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class DiscardInformationBean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4525188291406111486L;
	/**
	 * 原报文发起人
	 */
	@JSONField(name="OrigSender")
	private String origSender;
	/**
	 * 原报文类型代码
	 */
	@JSONField(name="MsgType")
	private String msgType;
	/**
	 * 原报文通信参考号
	 */
	@JSONField(name="RefId")
	private String refId;
	/**
	 * 业务拒绝码
	 */
	@JSONField(name="RjctCd")
	private String rjctCd;
	/**
	 * 业务拒绝信息
	 */
	@JSONField(name="RjctInf")
	private String rjctInf;
	public String getOrigSender() {
		return origSender;
	}
	public void setOrigSender(String origSender) {
		this.origSender = origSender;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getRjctCd() {
		return rjctCd;
	}
	public void setRjctCd(String rjctCd) {
		this.rjctCd = rjctCd;
	}
	public String getRjctInf() {
		return rjctInf;
	}
	public void setRjctInf(String rjctInf) {
		this.rjctInf = rjctInf;
	}
	
	
}
