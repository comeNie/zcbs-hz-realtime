package org.hz.realtime.business.message.enums;

public enum BusinessType {
    // TODO todomxw
    REAL_TIME_COLL("XXXX", "实时代收"),
    REAL_TIME_PAY("", "实时代付"), ;

    private String value;
    private final String displayName;

    BusinessType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static BusinessType parseOf(String value) {

        for (BusinessType item : values())
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
