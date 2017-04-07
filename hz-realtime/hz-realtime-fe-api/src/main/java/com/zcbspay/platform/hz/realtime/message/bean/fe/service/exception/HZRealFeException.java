package com.zcbspay.platform.hz.realtime.message.bean.fe.service.exception;

import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.ErrorCodeFeHZ;

/**
 * 杭州清算中心渠道，支付渠道封装的异常信息
 * 
 * @author AlanMa
 *
 */
public class HZRealFeException extends Exception {

    private static final long serialVersionUID = 8564355573270250157L;

    private String errCode;

    private String errMsg;

    public HZRealFeException() {
        super();
    }

    public HZRealFeException(String errCode, String errMsg) {
        super(errCode + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public HZRealFeException(String errCode) {
        super(errCode + ErrorCodeFeHZ.parseOf(errCode).getDisplayName());
        this.errCode = errCode;
        this.errMsg = ErrorCodeFeHZ.parseOf(errCode).getDisplayName();
    }

    public HZRealFeException(ErrorCodeFeHZ errorCode) {
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
