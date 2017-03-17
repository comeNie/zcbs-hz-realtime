package com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean;

public class SendResult {

    private boolean isSentSuc;
    private String errorMsg;

    public SendResult() {
        super();
        this.isSentSuc = true;
    }

    public SendResult(String errorMsg) {
        super();
        this.isSentSuc = false;
        this.errorMsg = errorMsg;
    }

    public boolean isSentSuc() {
        return isSentSuc;
    }

    public void setSentSuc(boolean isSentSuc) {
        this.isSentSuc = isSentSuc;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "SendResult [isSentSuc=" + isSentSuc + ", errorMsg=" + errorMsg + "]";
    }

}
