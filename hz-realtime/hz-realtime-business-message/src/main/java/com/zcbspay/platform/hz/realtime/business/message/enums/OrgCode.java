package com.zcbspay.platform.hz.realtime.business.message.enums;

public enum OrgCode {

    // ZCBS("XXXXXXXXXX", "中创标石机构号"),发送机构不是zcbs，而是各个商户在清算中心开的账户
    DEFAUT("3310961099", "XX机构"),//仅在通信探测报文中使用
    HZQSZX("3310100102", "杭州清算中心机构号"), ;

    private String value;
    private final String displayName;

    OrgCode(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static OrgCode parseOf(String value) {

        for (OrgCode item : values())
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
