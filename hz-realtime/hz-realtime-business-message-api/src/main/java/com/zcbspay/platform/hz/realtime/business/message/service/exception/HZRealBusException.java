package com.zcbspay.platform.hz.realtime.business.message.service.exception;

import com.zcbspay.platform.hz.realtime.business.message.service.enums.ErrorCodeBusHZ;


/**
 * 杭州清算中心渠道，支付渠道封装的异常信息
 * 
 * @author AlanMa
 *
 */
public class HZRealBusException extends Exception {

    private static final long serialVersionUID = 8564355573270250157L;

    private String errCode;

    private String errMsg;

    public HZRealBusException() {
        super();
    }

    public HZRealBusException(String errCode, String errMsg) {
        super(errCode + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public HZRealBusException(String errCode) {
        super(errCode + ErrorCodeBusHZ.parseOf(errCode).getDisplayName());
        this.errCode = errCode;
        this.errMsg = ErrorCodeBusHZ.parseOf(errCode).getDisplayName();
    }

    public HZRealBusException(ErrorCodeBusHZ errorCode) {
        super(errorCode.getValue() + errorCode.getDisplayName());
        this.errCode = errorCode.getValue();
        this.errMsg = errorCode.getDisplayName();
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "UnionPayException [errCode=" + errCode + ", errMsg=" + errMsg + "]";
    }

}
