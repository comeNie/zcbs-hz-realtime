package org.hz.realtime.business.message.bean;

import java.io.Serializable;

public class RealTimeCollRespBean implements Serializable {

    private static final long serialVersionUID = 4049095469198828235L;
    /**
     * 原报文主键
     */
    private OriginalMessageIdent OrgnlMsgId;
    /**
     * 业务应答信息
     */
    private ResponseInfo RspnInf;
    /**
     * 业务块内容
     */
    private BusiTextRtCollResp BusiText;

    public OriginalMessageIdent getOrgnlMsgId() {
        return OrgnlMsgId;
    }

    public void setOrgnlMsgId(OriginalMessageIdent orgnlMsgId) {
        OrgnlMsgId = orgnlMsgId;
    }

    public ResponseInfo getRspnInf() {
        return RspnInf;
    }

    public void setRspnInf(ResponseInfo rspnInf) {
        RspnInf = rspnInf;
    }

    public BusiTextRtCollResp getBusiText() {
        return BusiText;
    }

    public void setBusiText(BusiTextRtCollResp busiText) {
        BusiText = busiText;
    }

}
