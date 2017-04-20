package com.zcbspay.platform.hz.realtime.business.message.service.enums;



/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年11月20日 上午11:31:21
 * @since 
 */
public enum ChnlTypeEnum {
    REAPAY("RPQP"),
    TC("TC"),
    TEST("TEST"),
    CMBCWITHHOLDING("CMBC"),
    BOSSPAY("BOSS"),
    CHANPAY("CHAN"),
    WECHAT("WECHAT"),
    HZ("HZ"),
    UNKNOW("99");
   private String tradeType;
    
    private ChnlTypeEnum(String tradeType){
        this.tradeType = tradeType;
    }
    
    public static ChnlTypeEnum fromValue(String value) {
        for(ChnlTypeEnum status:values()){
            if(status.tradeType==value){
                return status;
            }
        }
        return UNKNOW;
    }
    
    public String getTradeType(){
        return tradeType;
    }
}
