package com.zcbspay.platform.hz.realtime.business.message.enums;

public enum RealCPOrdSts {
    INITIAL("01", "初始，订单提交成功，但未支付"), 
    PAYING("02", "支付中"), 
    SCUCESS("00", "成功"), 
    FAILED("03", "失败"), ;

    private String value;
    private final String displayName;

    RealCPOrdSts(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static RealCPOrdSts parseOf(String value) {

        for (RealCPOrdSts item : values())
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
