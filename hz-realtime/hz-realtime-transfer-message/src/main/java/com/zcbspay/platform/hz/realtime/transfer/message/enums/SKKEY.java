package com.zcbspay.platform.hz.realtime.transfer.message.enums;

public enum SKKEY {

    HZCC_SHPT_PRIKEY("HZCC_RT_CP_SHPT_PRI_KEY", "杭州清算中心渠道实时代收付业务宜赋通私钥"), 
    HZCC_PUBKEY("HZCC_RT_CP_PUB_KEY", "杭州清算中心渠道实时代收付业务清算中心公钥"), 
    ;

    private String value;
    private final String displayName;

    SKKEY(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SKKEY parseOf(String value) {

        for (SKKEY item : values())
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
