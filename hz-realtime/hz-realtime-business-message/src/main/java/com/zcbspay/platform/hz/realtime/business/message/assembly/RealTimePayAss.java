package com.zcbspay.platform.hz.realtime.business.message.assembly;

import javax.annotation.Resource;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.message.bean.BusiTextBean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT386Bean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.MessageTypeEnum;

/**
 * 实时代付报文拼装
 *
 * @author alanma
 * @version
 * @date 2017年3月6日 上午9:52:07
 * @since
 */
public class RealTimePayAss {

    @Resource(name="redisSerialNumberService")
    private static SerialNumberService redisSerialNumberService;

    public static MessageBean realtimePayMsgBodyReq(SinglePaymentBean paymentBean) {
        MessageBean msgBean = new MessageBean();
        msgBean.setMessageTypeEnum(MessageTypeEnum.CMT386);
        CMT386Bean bean = new CMT386Bean();
        bean.setMsgId(redisSerialNumberService.generateHZMsgId());
        BusiTextBean textBean = new BusiTextBean();
        textBean.setTxId(paymentBean.getTxId());
        textBean.setDbtrBk(paymentBean.getDebtorAgentCode());
        textBean.setDbtrAcct(paymentBean.getDebtorAccountNo());
        textBean.setDbtrNm(paymentBean.getDebtorName());
        textBean.setDbtrCnsn(paymentBean.getEndToEndIdentification());
        textBean.setCdtrBk(paymentBean.getCreditorAgentCode());
        textBean.setCdtrAcct(paymentBean.getCreditorAccountNo());
        textBean.setCdtrNm(paymentBean.getCreditorName());
        textBean.setAmt(paymentBean.getAmount());
        textBean.setPrtry(paymentBean.getPurposeCode());
        textBean.setSummary(paymentBean.getSummary());
        bean.setBusiText(textBean);
        msgBean.setMessageBean(bean);
        return msgBean;
    }

}
