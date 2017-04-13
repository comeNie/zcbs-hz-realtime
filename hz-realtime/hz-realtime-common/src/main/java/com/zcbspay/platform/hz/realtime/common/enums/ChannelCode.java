package com.zcbspay.platform.hz.realtime.common.enums;

public enum ChannelCode {

    CHL_HZQSZX("ZZZZZZZZ", "杭州清算中心渠道号"), 
    ;

    private String value;
    private final String displayName;

    ChannelCode(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ChannelCode parseOf(String value) {

        for (ChannelCode item : values())
            if (item.getValue().equals(value))
                return item;

        return null;
    }

    // public static void main(String[] args) {
    // System.out.println(ReturnInfo.SIGN_FAILED.getDisplayName());
    // System.out.println(ReturnInfo.SIGN_FAILED.getValue());
    // System.out.println(parseOf(ReturnInfo.SIGN_FAILED.getValue()).getDisplayName());
    // System.out.print(ReturnInfo.SIGN_FAILED);
    // }
}
