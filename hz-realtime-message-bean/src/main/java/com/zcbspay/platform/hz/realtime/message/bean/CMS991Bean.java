package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CMS991Bean implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7128376821535864118L;
	@JSONField(name="MsgId")
	private String msgId;
	@JSONField(name="ChckInf")
	private CheckInformationBean checkInformationBean;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public CheckInformationBean getCheckInformationBean() {
		return checkInformationBean;
	}
	public void setCheckInformationBean(CheckInformationBean checkInformationBean) {
		this.checkInformationBean = checkInformationBean;
	}
	
	
}
