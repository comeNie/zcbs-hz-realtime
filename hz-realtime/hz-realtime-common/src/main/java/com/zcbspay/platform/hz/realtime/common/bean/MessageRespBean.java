package com.zcbspay.platform.hz.realtime.common.bean;

import java.io.Serializable;

/**
 * 回执报文
 * 
 * @author AlanMa
 *
 */
public class MessageRespBean implements Serializable {

    private static final long serialVersionUID = -4208601564626699443L;
    // 报文头
    private MessageHeaderBean messageHeaderBean;
    // 报文体Json字符串
    private String msgBody;

    public MessageHeaderBean getMessageHeaderBean() {
        return messageHeaderBean;
    }

    public void setMessageHeaderBean(MessageHeaderBean messageHeaderBean) {
        this.messageHeaderBean = messageHeaderBean;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    @Override
    public String toString() {
        return "MessageRespBean [messageHeaderBean=" + messageHeaderBean + ", msgBody=" + msgBody + "]";
    }

}
