package com.zcbspay.platform.hz.realtime.transfer.message.api.bean;

import java.io.Serializable;

import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.MessageTypeEnum;

public class MessageBean implements Serializable {

    private static final long serialVersionUID = -307701236779309629L;

    private MessageTypeEnum messageTypeEnum;

    private Object messageBean;

    public MessageBean() {
        super();
    }

    public MessageBean(MessageTypeEnum messageTypeEnum, Object messageBean) {
        super();
        this.messageTypeEnum = messageTypeEnum;
        this.messageBean = messageBean;
    }

    public MessageTypeEnum getMessageTypeEnum() {
        return messageTypeEnum;
    }

    public Object getMessageBean() {
        return messageBean;
    }

    public void setMessageTypeEnum(MessageTypeEnum messageTypeEnum) {
        this.messageTypeEnum = messageTypeEnum;
    }

    public void setMessageBean(Object messageBean) {
        this.messageBean = messageBean;
    }

    public MessageBean(Object messageBean, MessageTypeEnum messageTypeEnum) {
        this.messageBean = messageBean;
        this.messageTypeEnum = messageTypeEnum;
    }

    @Override
    public String toString() {
        return "MessageBean [messageTypeEnum=" + messageTypeEnum + ", messageBean=" + messageBean + "]";
    }
    

}
