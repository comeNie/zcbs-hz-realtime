package org.hz.realtime.business.message.impl;

import javax.annotation.Resource;

import org.hz.realtime.business.message.assembly.BusStatQryAss;
import org.hz.realtime.business.message.assembly.RealTimeCollAss;
import org.hz.realtime.business.message.assembly.RealTimePayAss;
import org.hz.realtime.business.message.dao.TChnCollectSingleLogDAO;
import org.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import org.hz.realtime.business.message.enums.OrgCode;
import org.hz.realtime.business.message.enums.ReturnInfo;
import org.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;
import org.hz.realtime.business.message.pojo.TChnPaymentSingleLogDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zcbspay.platform.hz.realtime.business.message.service.BusinesssMessageSender;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.enums.ErrorCodeHZ;
import com.zcbspay.platform.hz.realtime.message.bean.CMT384Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT386Bean;
import com.zcbspay.platform.hz.realtime.message.bean.OrgnlMsgIdBean;
import com.zcbspay.platform.hz.realtime.message.bean.OrgnlTxBean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.MessageSend;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBeanStr;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;
import com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageAssemble;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;

public class BusinesssMessageSenderImpl implements BusinesssMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(BusinesssMessageSenderImpl.class);

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
        // 交易判重
        TChnCollectSingleLogDO record = tChnCollectSingleLogDAO.getCollSingleByTxnseqno(collectionChargesBean.getTxnseqno());
        if (record != null) {
            logger.error("repeat request and txnseqno is : " + collectionChargesBean.getTxnseqno());
            return new ResultBean(ErrorCodeHZ.REPEAT_REQUEST.getValue(), ErrorCodeHZ.REPEAT_REQUEST.getDisplayName());
        }
        // CMT384报文组装
        MessageHeaderBean beanHead = RealTimeCollAss.realtimeCollMsgHeaderReq(collectionChargesBean);
        MessageBean beanBody = RealTimeCollAss.realtimeCollMsgBodyReq(collectionChargesBean);
        String message = messageAssemble.assemble(beanHead, beanBody);
        // 记录报文流水信息
        CMT384Bean bean = (CMT384Bean) beanBody.getMessageBean();
        tChnCollectSingleLogDAO.saveRealCollectLog(collectionChargesBean, bean.getMsgId());
        // 发送报文
        MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMT384);
        messageSendHZ.sendMessage(messageBean);
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean realTimePayment(SinglePaymentBean paymentBean) {
        // 交易判重
        TChnPaymentSingleLogDO record = tChnPaymentSingleLogDAO.getPaySingleByTxnseqno(paymentBean.getTxnseqno());
        if (record != null) {
            logger.error("repeat request and txnseqno is : " + paymentBean.getTxnseqno());
            return new ResultBean(ErrorCodeHZ.REPEAT_REQUEST.getValue(), ErrorCodeHZ.REPEAT_REQUEST.getDisplayName());
        }
        // CMT386报文组装
        MessageHeaderBean beanHead = RealTimePayAss.realtimePayMsgHeaderReq(paymentBean);
        MessageBean beanBody = RealTimePayAss.realtimePayMsgBodyReq(paymentBean);
        String message = messageAssemble.assemble(beanHead, beanBody);
        // 记录报文流水信息
        CMT386Bean bean = (CMT386Bean) beanBody.getMessageBean();
        tChnPaymentSingleLogDAO.saveRealPaymentLog(paymentBean, bean.getMsgId());
        MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMT384);
        messageSendHZ.sendMessage(messageBean);
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean queryTrade(String txnseqno, com.zcbspay.platform.hz.realtime.business.message.service.enums.MessageTypeEnum messageTypeEnum) {
        // 查询原交易获取原报文三要素
        OrgnlTxBean orgMsgIde = null;
        if (com.zcbspay.platform.hz.realtime.business.message.service.enums.MessageTypeEnum.CMT384.equals(messageTypeEnum)) {
            // 实时代收原交易
            TChnCollectSingleLogDO collSingle = tChnCollectSingleLogDAO.getCollSingleByTxnseqno(txnseqno);
            if (collSingle == null) {
                logger.error("cann't find record by txnseqno : " + txnseqno);
                return new ResultBean(ErrorCodeHZ.NONE_RECORD.getValue(), ErrorCodeHZ.NONE_RECORD.getDisplayName());
            }
            orgMsgIde = new OrgnlTxBean(OrgCode.ZCBS.getValue(), collSingle.getTxid(), MessageTypeEnum.CMT384.value());
        }
        else if (com.zcbspay.platform.hz.realtime.business.message.service.enums.MessageTypeEnum.CMT386.equals(messageTypeEnum)) {
            // 实时代付原交易
            TChnPaymentSingleLogDO paySingle = tChnPaymentSingleLogDAO.getPaySingleByTxnseqno(txnseqno);
            if (paySingle == null) {
                logger.error("cann't find record by txnseqno : " + txnseqno);
                return new ResultBean(ErrorCodeHZ.NONE_RECORD.getValue(), ErrorCodeHZ.NONE_RECORD.getDisplayName());
            }
            orgMsgIde = new OrgnlTxBean(OrgCode.ZCBS.getValue(), paySingle.getTxid(), MessageTypeEnum.CMT386.value());
        }
        // CMS316报文组装
        MessageHeaderBean beanHead = BusStatQryAss.busStatusQryMsgHeaderReq();
        MessageBean beanBody = BusStatQryAss.busStatusQryMsgBodyReq(orgMsgIde);
        String message = messageAssemble.assemble(beanHead, beanBody);
        // 记录报文流水信息
        MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMS316);
        messageSendHZ.sendMessage(messageBean);
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean check() {
        // TODO Auto-generated method stub
        return null;
    }

}
