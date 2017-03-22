package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class OrgnlTxBean implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1879721222357231049L;
	/**
	 * 原业务发起方
	 */
	@JSONField(name="OrgnlSender")
	private String orgnlSender;
	/**
	 * 原明细标识号
	 */
	@JSONField(name="OrgnlTxId")
	private String orgnlTxId;
	/**
	 * 原业务类型编码
	 */
	@JSONField(name="OrgnlMsgType")
	private String orgnlMsgType;
	
	public OrgnlTxBean() {
        super();
    }
    public OrgnlTxBean(String orgnlSender, String orgnlTxId, String orgnlMsgType) {
        super();
        this.orgnlSender = orgnlSender;
        this.orgnlTxId = orgnlTxId;
        this.orgnlMsgType = orgnlMsgType;
    }
    public String getOrgnlSender() {
		return orgnlSender;
	}
	public void setOrgnlSender(String orgnlSender) {
		this.orgnlSender = orgnlSender;
	}
	public String getOrgnlTxId() {
		return orgnlTxId;
	}
	public void setOrgnlTxId(String orgnlTxId) {
		this.orgnlTxId = orgnlTxId;
	}
	public String getOrgnlMsgType() {
		return orgnlMsgType;
	}
	public void setOrgnlMsgType(String orgnlMsgType) {
		this.orgnlMsgType = orgnlMsgType;
	}
	
	
}
