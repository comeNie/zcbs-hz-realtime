package com.zcbspay.platform.hz.realtime.message.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class BusiTextBean implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6907409956784428649L;
    @JSONField(name = "TxId")
    private String txId;
    @JSONField(name = "DbtrBk")
    private String dbtrBk;
    @JSONField(name = "DbtrAcct")
    private String dbtrAcct;
    @JSONField(name = "DbtrNm")
    private String dbtrNm;
    @JSONField(name = "DbtrCnsn")
    private String dbtrCnsn;
    @JSONField(name = "CdtrBk")
    private String cdtrBk;
    @JSONField(name = "CdtrAcct")
    private String cdtrAcct;
    @JSONField(name = "CdtrNm")
    private String cdtrNm;
    @JSONField(name = "Amt")
    private String amt;
    @JSONField(name = "Prtry")
    private String prtry;
    @JSONField(name = "Summary")
    private String summary;
    @JSONField(name = "BllNb")
    private String bllNb;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getDbtrBk() {
        return dbtrBk;
    }

    public void setDbtrBk(String dbtrBk) {
        this.dbtrBk = dbtrBk;
    }

    public String getDbtrAcct() {
        return dbtrAcct;
    }

    public void setDbtrAcct(String dbtrAcct) {
        this.dbtrAcct = dbtrAcct;
    }

    public String getDbtrNm() {
        return dbtrNm;
    }

    public void setDbtrNm(String dbtrNm) {
        this.dbtrNm = dbtrNm;
    }

    public String getDbtrCnsn() {
        return dbtrCnsn;
    }

    public void setDbtrCnsn(String dbtrCnsn) {
        this.dbtrCnsn = dbtrCnsn;
    }

    public String getCdtrBk() {
        return cdtrBk;
    }

    public void setCdtrBk(String cdtrBk) {
        this.cdtrBk = cdtrBk;
    }

    public String getCdtrAcct() {
        return cdtrAcct;
    }

    public void setCdtrAcct(String cdtrAcct) {
        this.cdtrAcct = cdtrAcct;
    }

    public String getCdtrNm() {
        return cdtrNm;
    }

    public void setCdtrNm(String cdtrNm) {
        this.cdtrNm = cdtrNm;
    }

    public String getPrtry() {
        return prtry;
    }

    public void setPrtry(String prtry) {
        this.prtry = prtry;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBllNb() {
        return bllNb;
    }

    public void setBllNb(String bllNb) {
        this.bllNb = bllNb;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

}
