package org.hz.realtime.business.message.bean;

import java.io.Serializable;

public class OriginalMessageIdent implements Serializable {

    private static final long serialVersionUID = 1956780187402131346L;
    /**
     * 原业务发起方
     */
    private String OrgnlSender;
    /**
     * 原报文标识号
     */
    private String OrgnlMsgId;
    /**
     * 原业务类型编码
     */
    private String OrgnlMsgType;

    public String getOrgnlSender() {
        return OrgnlSender;
    }

    public void setOrgnlSender(String orgnlSender) {
        OrgnlSender = orgnlSender;
    }

    public String getOrgnlMsgId() {
        return OrgnlMsgId;
    }

    public void setOrgnlMsgId(String orgnlMsgId) {
        OrgnlMsgId = orgnlMsgId;
    }

    public String getOrgnlMsgType() {
        return OrgnlMsgType;
    }

    public void setOrgnlMsgType(String orgnlMsgType) {
        OrgnlMsgType = orgnlMsgType;
    }

    @Override
    public String toString() {
        return "OriginalMessageIdent [OrgnlSender=" + OrgnlSender + ", OrgnlMsgId=" + OrgnlMsgId + ", OrgnlMsgType=" + OrgnlMsgType + "]";
    }

}
