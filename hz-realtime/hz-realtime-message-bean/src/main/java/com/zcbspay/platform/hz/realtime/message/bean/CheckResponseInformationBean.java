package com.zcbspay.platform.hz.realtime.message.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class CheckResponseInformationBean {

	/**
	 * 中心工作日期
	 */
	@JSONField(name="WorkDate")
	private String workDate;

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	
	
}
