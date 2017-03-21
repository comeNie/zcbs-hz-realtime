package org.hz.realtime.business.message.impl;

import javax.annotation.Resource;

import org.hz.realtime.business.message.assembly.RealTimeCollAss;
import org.hz.realtime.business.message.assembly.RealTimePayAss;
import org.hz.realtime.business.message.dao.TChnCollectSingleLogDAO;
import org.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import org.hz.realtime.business.message.enums.ReturnInfo;
import org.springframework.beans.factory.annotation.Autowired;

import com.zcbspay.platform.hz.realtime.business.message.service.BusinesssMessageSender;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.MessageSend;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBeanStr;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;
import com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageAssemble;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;

public class BusinesssMessageSenderImpl implements BusinesssMessageSender {

    @Resource
    private MessageSend messageSendHZ;
    @Resource
    private MessageAssemble messageAssemble;
    @Autowired
    private TChnCollectSingleLogDAO tChnCollectSingleLogDAO;
    @Autowired
    private TChnPaymentSingleLogDAO tChnPaymentSingleLogDAO;

    @Override
    public ResultBean realTimeCollectionCharges(SingleCollectionChargesBean collectionChargesBean) {
        // CMT384报文组装
        MessageHeaderBean beanHead = RealTimeCollAss.realtimeCollMsgHeaderReq(collectionChargesBean);
        MessageBean beanBody = RealTimeCollAss.realtimeCollMsgBodyReq(collectionChargesBean);
        String message = messageAssemble.assemble(beanHead, beanBody);
        // 记录报文流水信息
        tChnCollectSingleLogDAO.saveRealCollectLog(collectionChargesBean);
        // 发送报文
        MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMT384);
        messageSendHZ.sendMessage(messageBean);
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean realTimePayment(SinglePaymentBean paymentBean) {
        // CMT386报文组装
        MessageHeaderBean beanHead = RealTimePayAss.realtimeCollMsgHeaderReq(paymentBean);
        MessageBean beanBody = RealTimePayAss.realtimeCollMsgBodyReq(paymentBean);
        String message = messageAssemble.assemble(beanHead, beanBody);
        // 记录报文流水信息
        tChnPaymentSingleLogDAO.saveRealPaymentLog(paymentBean);
        MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMT384);
        messageSendHZ.sendMessage(messageBean);
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
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
