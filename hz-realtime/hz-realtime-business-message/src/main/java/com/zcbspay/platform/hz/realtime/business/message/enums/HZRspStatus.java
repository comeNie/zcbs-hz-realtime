package com.zcbspay.platform.hz.realtime.business.message.enums;

public enum HZRspStatus {

    UNKNOWN("PRXX", "未知"), 
    TRANSFER("PR00", "已转发"),
    SUCCESS("PR05", "已成功"), 
    REJECTED("PR09", "已拒绝"), 
    OVERDUE("PR32", "逾期退回"), 
    ;

    private String value;
    private final String displayName;

    HZRspStatus(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static HZRspStatus parseOf(String value) {

        for (HZRspStatus item : values())
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
