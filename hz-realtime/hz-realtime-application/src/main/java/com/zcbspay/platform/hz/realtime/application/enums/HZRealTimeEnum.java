package com.zcbspay.platform.hz.realtime.application.enums;


public enum HZRealTimeEnum {
	/**
	 * 实时代付
	 */
	REALTIME_PAYMENT("TAG_001"),
	/**
	 * 实时代收
	 */
	REALTIME_COLLECT("TAG_002"),
	/**
	 * 交易查询
	 */
	TRADE_QUERY("TAG_003"),
	/**
	 * 链路探测
	 */
	LINK_CHECK("TAG_004")
	;
	private String code;

	/**
	 * @param code
	 */
	private HZRealTimeEnum(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	
	public static HZRealTimeEnum fromValue(String code){
		for(HZRealTimeEnum tagsEnum : values()){
			if(tagsEnum.getCode().equals(code)){
				return tagsEnum;
			}
		}
		return null;
	}
}
