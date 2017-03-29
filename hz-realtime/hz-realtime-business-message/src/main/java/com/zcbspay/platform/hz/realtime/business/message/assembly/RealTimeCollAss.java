package com.zcbspay.platform.hz.realtime.business.message.assembly;

import javax.annotation.Resource;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.message.bean.BusiTextBean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT384Bean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.MessageTypeEnum;

/**
 * 实时代收报文拼装
 *
 * @author alanma
 * @version
 * @date 2017年3月6日 上午9:52:07
 * @since
 */
public class RealTimeCollAss {

    @Resource(name="redisSerialNumberService")
    private static SerialNumberService redisSerialNumberService;

    public static MessageBean realtimeCollMsgBodyReq(SingleCollectionChargesBean collectionChargesBean) {
        MessageBean msgBean = new MessageBean();
        msgBean.setMessageTypeEnum(MessageTypeEnum.CMT384);
        CMT384Bean bean = new CMT384Bean();
        bean.setMsgId(redisSerialNumberService.generateHZMsgId());
        BusiTextBean textBean = new BusiTextBean();
        textBean.setTxId(collectionChargesBean.getTxId());
        textBean.setDbtrBk(collectionChargesBean.getDebtorAgentCode());
        textBean.setDbtrAcct(collectionChargesBean.getDebtorAccountNo());
        textBean.setDbtrNm(collectionChargesBean.getDebtorName());
        textBean.setDbtrCnsn(collectionChargesBean.getEndToEndIdentification());
        textBean.setCdtrBk(collectionChargesBean.getCreditorAgentCode());
        textBean.setCdtrAcct(collectionChargesBean.getCreditorAccountNo());
        textBean.setCdtrNm(collectionChargesBean.getCreditorName());
        textBean.setAmt(collectionChargesBean.getAmount());
        textBean.setPrtry(collectionChargesBean.getPurposeCode());
        textBean.setSummary(collectionChargesBean.getSummary());
        bean.setBusiText(textBean);
        msgBean.setMessageBean(bean);
        return msgBean;
    }

}
