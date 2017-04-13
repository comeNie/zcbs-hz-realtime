package com.zcbspay.platform.hz.realtime.business.message.bean;

import java.io.Serializable;

import com.zcbspay.platform.hz.realtime.business.message.enums.BusinessType;

public class TransLogUpBean implements Serializable {

    private static final long serialVersionUID = 6892455130339690001L;

    private String payretcode;

    private String txnseqno;

    private String rspStatus;

    private BusinessType businessType;

    public String getPayretcode() {
        return payretcode;
    }

    public void setPayretcode(String payretcode) {
        this.payretcode = payretcode;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public String getTxnseqno() {
        return txnseqno;
    }

    public void setTxnseqno(String txnseqno) {
        this.txnseqno = txnseqno;
    }

    public String getRspStatus() {
        return rspStatus;
    }

    public void setRspStatus(String rspStatus) {
        this.rspStatus = rspStatus;
    }

    @Override
    public String toString() {
        return "TransLogUpBean [payretcode=" + payretcode + ", txnseqno=" + txnseqno + ", rspStatus=" + rspStatus + ", businessType=" + businessType + "]";
    }

}
