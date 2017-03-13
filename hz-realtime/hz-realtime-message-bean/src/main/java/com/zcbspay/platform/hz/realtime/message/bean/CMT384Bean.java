package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class CMT384Bean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7843139412126041179L;
	@JSONField(name="MsgId")
	private String msgId;
	@JSONField(name="BusiText")
	private BusiTextBean busiText;
	
	
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public BusiTextBean getBusiText() {
		return busiText;
	}
	public void setBusiText(BusiTextBean busiText) {
		this.busiText = busiText;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return JSON.toJSONString(this);
	}
}
