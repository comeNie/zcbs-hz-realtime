package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CheckInformationBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2143803366914218377L;
	@JSONField(name="ChckFlg")
	private String checkFlag;

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	
	
}
