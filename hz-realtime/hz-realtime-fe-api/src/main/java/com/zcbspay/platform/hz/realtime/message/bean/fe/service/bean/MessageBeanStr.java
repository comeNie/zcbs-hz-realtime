package com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean;


public class MessageBeanStr {

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

}
