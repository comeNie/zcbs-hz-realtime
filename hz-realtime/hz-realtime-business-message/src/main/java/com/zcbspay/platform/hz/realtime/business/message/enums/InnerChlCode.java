package com.zcbspay.platform.hz.realtime.business.message.enums;

public enum InnerChlCode {
    REAL_TIME_COLL("10000001", "杭州实时代收付"), 
    REAL_TIME_PAY("10000002", "杭州批量代收付"), ;

    private String value;
    private final String displayName;

    InnerChlCode(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static InnerChlCode parseOf(String value) {

        for (InnerChlCode item : values())
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
