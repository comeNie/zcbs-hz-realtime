package com.zcbspay.platform.hz.realtime.application.exception;

/**
 * 杭州清算中心渠道，支付渠道封装的异常信息
 * 
 * @author AlanMa
 *
 */
public class HZRealAppException extends Exception {

    private static final long serialVersionUID = 4764118727636918785L;

    private String errCode;

    private String errMsg;

    public HZRealAppException() {
        super();
    }

    public HZRealAppException(String errCode, String errMsg) {
        super(errCode + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public HZRealAppException(String errCode) {
        super(errCode + ErrorCodeAppHZ.parseOf(errCode).getDisplayName());
        this.errCode = errCode;
        this.errMsg = ErrorCodeAppHZ.parseOf(errCode).getDisplayName();
    }

    public HZRealAppException(ErrorCodeAppHZ errorCode) {
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
