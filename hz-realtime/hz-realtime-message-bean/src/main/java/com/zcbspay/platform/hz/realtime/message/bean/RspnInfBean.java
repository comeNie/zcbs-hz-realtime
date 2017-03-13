package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class RspnInfBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1486796214556412187L;
	@JSONField(name="Sts")
	private String sts;
	@JSONField(name="RjctCd")
	private String rjctcd;
	@JSONField(name="RjctInf")
	private String rjctinf;
	@JSONField(name="NetgDt")
	private String netgdt;
	
	public String getSts() {
		return sts;
	}
	public void setSts(String sts) {
		this.sts = sts;
	}
	public String getRjctcd() {
		return rjctcd;
	}
	public void setRjctcd(String rjctcd) {
		this.rjctcd = rjctcd;
	}
	public String getRjctinf() {
		return rjctinf;
	}
	public void setRjctinf(String rjctinf) {
		this.rjctinf = rjctinf;
	}
	public String getNetgdt() {
		return netgdt;
	}
	public void setNetgdt(String netgdt) {
		this.netgdt = netgdt;
	}
	
	
}
