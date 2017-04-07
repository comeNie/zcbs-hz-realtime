package com.zcbspay.platform.hz.realtime.transfer.message.api.enums;

public enum ErrorCodeTransHZ {

	SIGN_FAILED("THZ001", "报文体加签失败"),
	NONE_RECORD("THZ002", "交易流水号无对应代收/代付记录"),
	REPEAT_REQUEST("THZ003", "交易重复，拒绝本次请求"),
	CHECK_SIGN_FAIL("THZ004", "应答报文验签失败"),
	BYTE_PARSE_FAIL("THZ005", "字符转换异常"),
	;

	private String value;
	private final String displayName;

	ErrorCodeTransHZ(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public String getValue() {
		return value;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static ErrorCodeTransHZ parseOf(String value) {

		for (ErrorCodeTransHZ item : values())
			if (item.getValue().equals(value))
				return item;

		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(ErrorCodeTransHZ.SIGN_FAILED.getDisplayName());
		System.out.println(ErrorCodeTransHZ.SIGN_FAILED.getValue());
		System.out.println(parseOf(ErrorCodeTransHZ.SIGN_FAILED.getValue()).getDisplayName());
		System.out.print(ErrorCodeTransHZ.SIGN_FAILED);
	}
}
