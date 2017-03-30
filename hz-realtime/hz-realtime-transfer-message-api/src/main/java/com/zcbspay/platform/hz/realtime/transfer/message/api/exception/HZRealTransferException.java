/* 
 * BaseException.java  
 * 
 * version TODO
 *
 * 2015年9月6日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.hz.realtime.transfer.message.api.exception;

import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.ErrorCodeHZ;

/**
 * 杭州清算中心渠道，支付渠道封装的异常信息
 * @author AlanMa
 *
 */
public class HZRealTransferException extends Exception {

    private static final long serialVersionUID = 8564355573270250157L;

    private String errCode;

    private String errMsg;

    public HZRealTransferException() {
        super();
    }

    public HZRealTransferException(String errCode, String errMsg) {
        super(errCode + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public HZRealTransferException(String errCode) {
        super(errCode + ErrorCodeHZ.parseOf(errCode).getDisplayName());
        this.errCode = errCode;
        this.errMsg = ErrorCodeHZ.parseOf(errCode).getDisplayName();
    }

    public HZRealTransferException(ErrorCodeHZ errorCode) {
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
