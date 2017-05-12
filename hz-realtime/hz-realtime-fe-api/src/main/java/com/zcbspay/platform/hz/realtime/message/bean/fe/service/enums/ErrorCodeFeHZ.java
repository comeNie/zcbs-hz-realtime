package com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums;

public enum ErrorCodeFeHZ {

    SEND_FAILED("THZF201", "发送报文至杭州清算中心失败"), 
    ;

    private String value;
    private final String displayName;

    ErrorCodeFeHZ(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ErrorCodeFeHZ parseOf(String value) {

        for (ErrorCodeFeHZ item : values())
            if (item.getValue().equals(value))
                return item;

        return null;
    }

}
