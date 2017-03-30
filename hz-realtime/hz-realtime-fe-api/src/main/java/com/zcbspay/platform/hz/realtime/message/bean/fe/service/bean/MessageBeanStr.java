package com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean;

import java.io.Serializable;

import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;

public class MessageBeanStr implements Serializable {

    private static final long serialVersionUID = -7572376438853746664L;
    /**
     * 要发送报文
     */
    private String sendMsg;
    /**
     * 报文类型
     */
    private MessageTypeEnum messageType;

    public MessageBeanStr() {
        super();
    }

    public MessageBeanStr(String sendMsg, MessageTypeEnum messageType) {
        super();
        this.sendMsg = sendMsg;
        this.messageType = messageType;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public MessageTypeEnum getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypeEnum messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "MessageBeanStr [sendMsg=" + sendMsg + ", messageType=" + messageType + "]";
    }
    
    

}
