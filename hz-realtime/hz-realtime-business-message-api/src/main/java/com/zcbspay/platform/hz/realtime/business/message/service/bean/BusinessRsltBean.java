package com.zcbspay.platform.hz.realtime.business.message.service.bean;

import java.io.Serializable;


/**
 * 实时代收付业务应答信息
 * @author AlanMa
 *
 */
public class BusinessRsltBean  implements Serializable{

    private static final long serialVersionUID = -701603694795725487L;

    /**
     * 报文标识号
     */
    private String msgid;
    /**
     * 交易流水号
     */
    private String txnseqno;
    /**
     * 通用回执状态
     */
    private String commRspSts;
    /**
     * 通用回执码
     */
    private String commRspCode;
    /**
     * 通用回执信息
     */
    private String commRspInfo;
    
    public String getMsgid() {
        return msgid;
    }
    
    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
    
    public String getTxnseqno() {
        return txnseqno;
    }
    
    public void setTxnseqno(String txnseqno) {
        this.txnseqno = txnseqno;
    }
    
    public String getCommRspSts() {
        return commRspSts;
    }
    
    public void setCommRspSts(String commRspSts) {
        this.commRspSts = commRspSts;
    }
    
    public String getCommRspCode() {
        return commRspCode;
    }
    
    public void setCommRspCode(String commRspCode) {
        this.commRspCode = commRspCode;
    }
    
    public String getCommRspInfo() {
        return commRspInfo;
    }
    
    public void setCommRspInfo(String commRspInfo) {
        this.commRspInfo = commRspInfo;
    }

    @Override
    public String toString() {
        return "BusinessRsltBean [msgid=" + msgid + ", txnseqno=" + txnseqno + ", commRspSts=" + commRspSts + ", commRspCode=" + commRspCode + ", commRspInfo=" + commRspInfo + "]";
    }
    
}
