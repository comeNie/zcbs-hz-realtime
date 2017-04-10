package com.zcbspay.platform.hz.realtime.business.message.service.enums;

public enum ErrorCodeBusHZ {

    SIGN_FAILED("THZ001", "报文体加签失败"), 
    NONE_RECORD("THZ002", "交易流水号无对应代收/代付记录"), 
    REPEAT_REQUEST("THZ003", "交易重复，拒绝本次请求"), 
    INTERRUPT_EXP("THZ004", "线程异常中断"), 
    NONE_MAIN_REC("THZ005", "交易流水号无对应主流水记录"), 
    UNKNOWN_BT("THZ006", "未知业务类型（非实时代收付）"), 
    NONE_PAY_ORDER("THZ007", "实时代收付订单信息缺失"),
    NONE_PAY_LOG("THZ008", "实时代收付主流水信息缺失"),
    REPEAT_RESP("THZ009", "回执信息重复，拒绝本次请求"), 
    ;

    private String value;
    private final String displayName;

    ErrorCodeBusHZ(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ErrorCodeBusHZ parseOf(String value) {

        for (ErrorCodeBusHZ item : values())
            if (item.getValue().equals(value))
                return item;

        return null;
    }

     public static void main(String[] args) {
     System.out.println(ErrorCodeBusHZ.SIGN_FAILED.getDisplayName());
     System.out.println(ErrorCodeBusHZ.SIGN_FAILED.getValue());
     System.out.println(parseOf(ErrorCodeBusHZ.SIGN_FAILED.getValue()).getDisplayName());
     System.out.print(ErrorCodeBusHZ.SIGN_FAILED);
     }
}
