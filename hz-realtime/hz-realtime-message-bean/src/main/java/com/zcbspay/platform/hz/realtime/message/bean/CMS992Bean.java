package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CMS992Bean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8510727061475549941L;
	
	@JSONField(name="MsgId")
	private String msgId; 
	@JSONField(name="ChckRspnInf")
	private CheckResponseInformationBean responseInformationBean;
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public CheckResponseInformationBean getResponseInformationBean() {
		return responseInformationBean;
	}
	public void setResponseInformationBean(
			CheckResponseInformationBean responseInformationBean) {
		this.responseInformationBean = responseInformationBean;
	}
	
}
