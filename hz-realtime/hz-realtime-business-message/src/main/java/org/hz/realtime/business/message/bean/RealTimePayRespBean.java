package org.hz.realtime.business.message.bean;

import java.io.Serializable;

public class RealTimePayRespBean implements Serializable {

    private static final long serialVersionUID = -326246982125142033L;
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
    private BusiTextRtPayResp BusiText;

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

    
    public BusiTextRtPayResp getBusiText() {
        return BusiText;
    }

    
    public void setBusiText(BusiTextRtPayResp busiText) {
        BusiText = busiText;
    }

}
