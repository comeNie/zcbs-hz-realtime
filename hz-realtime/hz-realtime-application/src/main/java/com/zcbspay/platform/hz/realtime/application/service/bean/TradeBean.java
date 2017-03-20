package com.zcbspay.platform.hz.realtime.application.service.bean;

import java.io.Serializable;

public class TradeBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4650629913077511492L;
	private String tn;
	private String txnseqno;
	
	public String getTn() {
		return tn;
	}
	public void setTn(String tn) {
		this.tn = tn;
	}
	public String getTxnseqno() {
		return txnseqno;
	}
	public void setTxnseqno(String txnseqno) {
		this.txnseqno = txnseqno;
	}
	
	
}
