package com.zcbspay.platform.hz.realtime.application.exception;

public enum ErrorCodeAppHZ {

    SIGN_FAILED("THZB001", "报文体加签失败"), 
    NONE_RECORD("THZB002", "交易流水号无对应实时代收/代付记录"), 
    REPEAT_REQUEST("THZB003", "交易重复，拒绝本次请求"), 
    INTERRUPT_EXP("THZB004", "线程异常中断"), 
    NONE_MAIN_REC("THZB005", "交易流水号无对应主流水记录"), 
    UNKNOWN_BT("THZB006", "未知业务类型（非实时代收付）"), 
    NONE_PAY_ORDER("THZB007", "实时代收付订单信息缺失"),
    NONE_PAY_LOG("THZB008", "实时代收付主流水信息缺失"),
    REPEAT_RESP("THZB009", "回执信息重复，拒绝本次请求"), 
    ORDER_STS_WRONG("THZB010", "实时代收/代付订单状态非法"),
    CHL_SER_STS_WR("THZB011", "渠道流水状态非法"),
    RSP_NOT_MATCH("THZB012", "业务应答与原交易记录关键信息不匹配"),
    PAR_PARSE_FAIL("THZB013", "参数转换异常"),
    CONN_FAIL("THZB014", "网络未通，无法建立连接"),
    ;

    private String value;
    private final String displayName;

    ErrorCodeAppHZ(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ErrorCodeAppHZ parseOf(String value) {

        for (ErrorCodeAppHZ item : values())
            if (item.getValue().equals(value))
                return item;

        return null;
    }

     public static void main(String[] args) {
     System.out.println(ErrorCodeAppHZ.SIGN_FAILED.getDisplayName());
     System.out.println(ErrorCodeAppHZ.SIGN_FAILED.getValue());
     System.out.println(parseOf(ErrorCodeAppHZ.SIGN_FAILED.getValue()).getDisplayName());
     System.out.print(ErrorCodeAppHZ.SIGN_FAILED);
     }
}
