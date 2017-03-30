package com.zcbspay.platform.hz.realtime.transfer.message.api.enums;

public enum ErrorCodeHZ {

	SIGN_FAILED("THZ001", "报文体加签失败"),
	NONE_RECORD("THZ002", "交易流水号无对应代收/代付记录"),
	REPEAT_REQUEST("THZ003", "交易重复，拒绝本次请求"),
	CHECK_SIGN_FAIL("THZ004", "应答报文验签失败"),
	;

	private String value;
	private final String displayName;

	ErrorCodeHZ(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public String getValue() {
		return value;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static ErrorCodeHZ parseOf(String value) {

		for (ErrorCodeHZ item : values())
			if (item.getValue().equals(value))
				return item;

		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(ErrorCodeHZ.SIGN_FAILED.getDisplayName());
		System.out.println(ErrorCodeHZ.SIGN_FAILED.getValue());
		System.out.println(parseOf(ErrorCodeHZ.SIGN_FAILED.getValue()).getDisplayName());
		System.out.print(ErrorCodeHZ.SIGN_FAILED);
	}
}
