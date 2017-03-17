package com.zcbspay.platform.hz.realtime.common.enums;



public enum MessageTypeEnum {
	//小额支付系统报文类型
    CMT384("CMT384"),
    CMT385("CMT385"),
    CMT386("CMT386"),
    CMT387("CMT387"),
	//公共控制系统报文类型
    CMS316("CMS316"),
    CMS317("CMS317"),
    CMS900("CMS900"),
    CMS911("CMS911"),
    CMS991("CMS991"),
    CMS992("CMS992"),
	;
	
	private String msgType;
	
	private MessageTypeEnum(String msgType){
		this.msgType = msgType;
	}
	
	public static MessageTypeEnum fromValue(String msgType) {
		for(MessageTypeEnum tagsEnum : values()){
			if(tagsEnum.value().equals(msgType)){
				return tagsEnum;
			}
		}
		return null;
    }
	public String value(){
		return msgType;
	}
}
