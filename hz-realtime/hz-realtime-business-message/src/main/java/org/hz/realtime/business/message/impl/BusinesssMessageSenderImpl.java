package org.hz.realtime.business.message.impl;

import javax.annotation.Resource;

import org.hz.realtime.business.message.assembly.RealTimeCollAss;

import com.zcbspay.platform.hz.realtime.business.message.service.BusinesssMessageSender;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.bean.MessageHeaderBean;
import com.zcbspay.platform.hz.realtime.common.enums.MessageTypeEnum;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.MessageSend;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBeanStr;
import com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageAssemble;

public class BusinesssMessageSenderImpl implements BusinesssMessageSender {

    @Resource
    private MessageSend messageSendHZ;
    @Resource
    private MessageAssemble messageAssemble;

    @Override
    public ResultBean realTimeCollectionCharges(SingleCollectionChargesBean collectionChargesBean) {

        // CMT384报文组装
        MessageHeaderBean beanHead = RealTimeCollAss.realtimeCollMsgHeaderReq(collectionChargesBean);
        MessageBean beanBody = RealTimeCollAss.realtimeCollMsgBodyReq(collectionChargesBean);
       /* String message = messageAssemble.assemble(beanHead, beanBody);
        // 记录报文流水信息
        
        // 发送报文
        MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMT384);
        messageSendHZ.sendMessage(messageBean);
*/
        return new ResultBean("SUCCESS");
    }

    @Override
    public ResultBean realTimePayment(SinglePaymentBean paymentBean) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultBean queryTrade(String txnseqno) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultBean check() {
        // TODO Auto-generated method stub
        return null;
    }

}
