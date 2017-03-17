package com.zcbspay.platform.hz.realtime.common.bean;

import java.io.Serializable;

/**
 * 报文头
 * @author AlanMa
 *
 */
public class MessageHeaderBean implements Serializable {

    private static final long serialVersionUID = -6015405245046018747L;
    // 报文体长度
    private String msgBodyLength;
    // 业务类型
    private String businessType;
    // 报文发起人代码
    private String sender;
    // 报文接收人代码
    private String reciever;
    // 报文发送时间
    private String sendTime;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgBodyLength() {
        return msgBodyLength;
    }

    public void setMsgBodyLength(String msgBodyLength) {
        this.msgBodyLength = msgBodyLength;
    }

    @Override
    public String toString() {
        return "MessageHeaderBean [msgBodyLength=" + msgBodyLength + ", businessType=" + businessType + ", sender=" + sender + ", reciever=" + reciever + ", sendTime=" + sendTime + "]";
    }

}
