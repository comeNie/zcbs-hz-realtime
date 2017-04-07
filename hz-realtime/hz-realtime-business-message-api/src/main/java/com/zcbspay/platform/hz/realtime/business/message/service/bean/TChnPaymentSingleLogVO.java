package com.zcbspay.platform.hz.realtime.business.message.service.bean;

public class TChnPaymentSingleLogVO implements java.io.Serializable {

    private static final long serialVersionUID = 8746399471650562552L;
    private long tid;
    private String msgid;
    private String receivetype;
    private String txid;
    private String transdate;
    private String transtime;
    private String debtorname;
    private String debtoraccountno;
    private String debtorbranchcode;
    private String creditorbranchcode;
    private String creditorname;
    private String creditoraccountno;
    private Long amount;
    private String purposeproprietary;
    private String endtoendidentification;
    private String billnumber;
    private String rspmsgid;
    private String rspstatus;
    private String rsprejectcode;
    private String rsprejectinformation;
    private String rspdate;
    private String nettingdate;
    private String commsgid;
    private String comstatus;
    private String comrejectcode;
    private String comrejectinformation;
    private String comdate;
    private String txnseqno;
    private String notes;
    private String remarks;

    public TChnPaymentSingleLogVO() {
    }

    public TChnPaymentSingleLogVO(long tid) {
        this.tid = tid;
    }

    public TChnPaymentSingleLogVO(long tid, String msgid, String receivetype, String txid, String transdate, String transtime, String debtorname, String debtoraccountno, String debtorbranchcode,
            String creditorbranchcode, String creditorname, String creditoraccountno, Long amount, String purposeproprietary, String endtoendidentification, String billnumber, String rspmsgid,
            String rspstatus, String rsprejectcode, String rsprejectinformation, String rspdate, String nettingdate, String commsgid, String comstatus, String comrejectcode,
            String comrejectinformation, String comdate, String txnseqno, String notes, String remarks) {
        this.tid = tid;
        this.msgid = msgid;
        this.receivetype = receivetype;
        this.txid = txid;
        this.transdate = transdate;
        this.transtime = transtime;
        this.debtorname = debtorname;
        this.debtoraccountno = debtoraccountno;
        this.debtorbranchcode = debtorbranchcode;
        this.creditorbranchcode = creditorbranchcode;
        this.creditorname = creditorname;
        this.creditoraccountno = creditoraccountno;
        this.amount = amount;
        this.purposeproprietary = purposeproprietary;
        this.endtoendidentification = endtoendidentification;
        this.billnumber = billnumber;
        this.rspmsgid = rspmsgid;
        this.rspstatus = rspstatus;
        this.rsprejectcode = rsprejectcode;
        this.rsprejectinformation = rsprejectinformation;
        this.rspdate = rspdate;
        this.nettingdate = nettingdate;
        this.commsgid = commsgid;
        this.comstatus = comstatus;
        this.comrejectcode = comrejectcode;
        this.comrejectinformation = comrejectinformation;
        this.comdate = comdate;
        this.txnseqno = txnseqno;
        this.notes = notes;
        this.remarks = remarks;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getReceivetype() {
        return receivetype;
    }

    public void setReceivetype(String receivetype) {
        this.receivetype = receivetype;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getTransdate() {
        return transdate;
    }

    public void setTransdate(String transdate) {
        this.transdate = transdate;
    }

    public String getTranstime() {
        return transtime;
    }

    public void setTranstime(String transtime) {
        this.transtime = transtime;
    }

    public String getDebtorname() {
        return debtorname;
    }

    public void setDebtorname(String debtorname) {
        this.debtorname = debtorname;
    }

    public String getDebtoraccountno() {
        return debtoraccountno;
    }

    public void setDebtoraccountno(String debtoraccountno) {
        this.debtoraccountno = debtoraccountno;
    }

    public String getDebtorbranchcode() {
        return debtorbranchcode;
    }

    public void setDebtorbranchcode(String debtorbranchcode) {
        this.debtorbranchcode = debtorbranchcode;
    }

    public String getCreditorbranchcode() {
        return creditorbranchcode;
    }

    public void setCreditorbranchcode(String creditorbranchcode) {
        this.creditorbranchcode = creditorbranchcode;
    }

    public String getCreditorname() {
        return creditorname;
    }

    public void setCreditorname(String creditorname) {
        this.creditorname = creditorname;
    }

    public String getCreditoraccountno() {
        return creditoraccountno;
    }

    public void setCreditoraccountno(String creditoraccountno) {
        this.creditoraccountno = creditoraccountno;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getPurposeproprietary() {
        return purposeproprietary;
    }

    public void setPurposeproprietary(String purposeproprietary) {
        this.purposeproprietary = purposeproprietary;
    }

    public String getEndtoendidentification() {
        return endtoendidentification;
    }

    public void setEndtoendidentification(String endtoendidentification) {
        this.endtoendidentification = endtoendidentification;
    }

    public String getBillnumber() {
        return billnumber;
    }

    public void setBillnumber(String billnumber) {
        this.billnumber = billnumber;
    }

    public String getRspmsgid() {
        return rspmsgid;
    }

    public void setRspmsgid(String rspmsgid) {
        this.rspmsgid = rspmsgid;
    }

    public String getRspstatus() {
        return rspstatus;
    }

    public void setRspstatus(String rspstatus) {
        this.rspstatus = rspstatus;
    }

    public String getRsprejectcode() {
        return rsprejectcode;
    }

    public void setRsprejectcode(String rsprejectcode) {
        this.rsprejectcode = rsprejectcode;
    }

    public String getRsprejectinformation() {
        return rsprejectinformation;
    }

    public void setRsprejectinformation(String rsprejectinformation) {
        this.rsprejectinformation = rsprejectinformation;
    }

    public String getRspdate() {
        return rspdate;
    }

    public void setRspdate(String rspdate) {
        this.rspdate = rspdate;
    }

    public String getNettingdate() {
        return nettingdate;
    }

    public void setNettingdate(String nettingdate) {
        this.nettingdate = nettingdate;
    }

    public String getCommsgid() {
        return commsgid;
    }

    public void setCommsgid(String commsgid) {
        this.commsgid = commsgid;
    }

    public String getComstatus() {
        return comstatus;
    }

    public void setComstatus(String comstatus) {
        this.comstatus = comstatus;
    }

    public String getComrejectcode() {
        return comrejectcode;
    }

    public void setComrejectcode(String comrejectcode) {
        this.comrejectcode = comrejectcode;
    }

    public String getComrejectinformation() {
        return comrejectinformation;
    }

    public void setComrejectinformation(String comrejectinformation) {
        this.comrejectinformation = comrejectinformation;
    }

    public String getComdate() {
        return comdate;
    }

    public void setComdate(String comdate) {
        this.comdate = comdate;
    }

    public String getTxnseqno() {
        return txnseqno;
    }

    public void setTxnseqno(String txnseqno) {
        this.txnseqno = txnseqno;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "TChnPaymentSingleLogVO [tid=" + tid + ", msgid=" + msgid + ", receivetype=" + receivetype + ", txid=" + txid + ", transdate=" + transdate + ", transtime=" + transtime
                + ", debtorname=" + debtorname + ", debtoraccountno=" + debtoraccountno + ", debtorbranchcode=" + debtorbranchcode + ", creditorbranchcode=" + creditorbranchcode + ", creditorname="
                + creditorname + ", creditoraccountno=" + creditoraccountno + ", amount=" + amount + ", purposeproprietary=" + purposeproprietary + ", endtoendidentification="
                + endtoendidentification + ", billnumber=" + billnumber + ", rspmsgid=" + rspmsgid + ", rspstatus=" + rspstatus + ", rsprejectcode=" + rsprejectcode + ", rsprejectinformation="
                + rsprejectinformation + ", rspdate=" + rspdate + ", nettingdate=" + nettingdate + ", commsgid=" + commsgid + ", comstatus=" + comstatus + ", comrejectcode=" + comrejectcode
                + ", comrejectinformation=" + comrejectinformation + ", comdate=" + comdate + ", txnseqno=" + txnseqno + ", notes=" + notes + ", remarks=" + remarks + "]";
    }

}
