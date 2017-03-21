package org.hz.realtime.business.message.bean;

import java.io.Serializable;

public class BusiTextRtPayResp implements Serializable {

    private static final long serialVersionUID = 7915318359151342055L;
    /**
     * 付款人银行号
     */
    private String DbtrBk;
    /**
     * 付款人账号
     */
    private String DbtrAcct;
    /**
     * 付款人名称
     */
    private String DbtrNm;
    /**
     * 收款人银行号
     */
    private String CdtrBk;
    /**
     * 收款人账号
     */
    private String CdtrAcct;
    /**
     * 收款人名称
     */
    private String CdtrNm;
    /**
     * 货币符号、金额
     */
    private String Amt;

    public String getDbtrBk() {
        return DbtrBk;
    }

    public void setDbtrBk(String dbtrBk) {
        DbtrBk = dbtrBk;
    }

    public String getDbtrAcct() {
        return DbtrAcct;
    }

    public void setDbtrAcct(String dbtrAcct) {
        DbtrAcct = dbtrAcct;
    }

    public String getDbtrNm() {
        return DbtrNm;
    }

    public void setDbtrNm(String dbtrNm) {
        DbtrNm = dbtrNm;
    }

    public String getCdtrBk() {
        return CdtrBk;
    }

    public void setCdtrBk(String cdtrBk) {
        CdtrBk = cdtrBk;
    }

    public String getCdtrAcct() {
        return CdtrAcct;
    }

    public void setCdtrAcct(String cdtrAcct) {
        CdtrAcct = cdtrAcct;
    }

    public String getCdtrNm() {
        return CdtrNm;
    }

    public void setCdtrNm(String cdtrNm) {
        CdtrNm = cdtrNm;
    }

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
    }

    @Override
    public String toString() {
        return "BusiTextRtCollResp [DbtrBk=" + DbtrBk + ", DbtrAcct=" + DbtrAcct + ", DbtrNm=" + DbtrNm + ", CdtrBk=" + CdtrBk + ", CdtrAcct=" + CdtrAcct + ", CdtrNm=" + CdtrNm + ", Amt=" + Amt + "]";
    }

}
