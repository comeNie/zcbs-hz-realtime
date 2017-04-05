package com.zcbspay.platform.hz.realtime.business.message.impl;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.business.message.assembly.BusStatQryAss;
import com.zcbspay.platform.hz.realtime.business.message.assembly.ComuDetecAss;
import com.zcbspay.platform.hz.realtime.business.message.assembly.MsgHeadAss;
import com.zcbspay.platform.hz.realtime.business.message.assembly.RealTimeCollAss;
import com.zcbspay.platform.hz.realtime.business.message.assembly.RealTimePayAss;
import com.zcbspay.platform.hz.realtime.business.message.dao.TChnCollectSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TxnsLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.enums.BusinessType;
import com.zcbspay.platform.hz.realtime.business.message.enums.OrgCode;
import com.zcbspay.platform.hz.realtime.business.message.enums.ReturnInfo;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TChnPaymentSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TTxnsLogDO;
import com.zcbspay.platform.hz.realtime.business.message.service.BusinesssMessageSender;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.enums.ErrorCodeHZ;
import com.zcbspay.platform.hz.realtime.message.bean.CMT384Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT386Bean;
import com.zcbspay.platform.hz.realtime.message.bean.OrgnlTxBean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.MessageSend;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBeanStr;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;
import com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageAssemble;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.exception.HZRealTransferException;

@Service("businesssMessageSender")
public class BusinesssMessageSenderImpl implements BusinesssMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(BusinesssMessageSenderImpl.class);

    @com.alibaba.dubbo.config.annotation.Reference(version = "1.0")
    private MessageSend messageSend;
    @com.alibaba.dubbo.config.annotation.Reference(version = "1.0")
    private MessageAssemble messageAssemble;
    @Autowired
    private TChnCollectSingleLogDAO tChnCollectSingleLogDAO;
    @Autowired
    private TChnPaymentSingleLogDAO tChnPaymentSingleLogDAO;
    @Autowired
    private TxnsLogDAO txnsLogDAO;

    @Override
    public ResultBean realTimeCollectionCharges(SingleCollectionChargesBean collectionChargesBean) {
        // 交易判重
        TChnCollectSingleLogDO record = tChnCollectSingleLogDAO.getCollSingleByTxnseqno(collectionChargesBean.getTxnseqno());
        if (record != null) {
            logger.error("repeat request and txnseqno is : " + collectionChargesBean.getTxnseqno());
            return new ResultBean(ErrorCodeHZ.REPEAT_REQUEST.getValue(), ErrorCodeHZ.REPEAT_REQUEST.getDisplayName());
        }
        // CMT384报文组装
        MessageHeaderBean beanHead = MsgHeadAss.commMsgHeaderReq(MessageTypeEnum.CMT384.value());
        logger.info("[beanHead is]:" + beanHead);
        MessageBean beanBody = RealTimeCollAss.realtimeCollMsgBodyReq(collectionChargesBean);
        logger.info("[beanBody is]:" + beanBody);
        byte[] message = null;
        try {
            message = messageAssemble.assemble(beanHead, beanBody);
        }
        catch (HZRealTransferException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        try {
            logger.info("[assembled message is]:" + new String(message, "utf-8"));
        }
        catch (UnsupportedEncodingException e) {
            logger.error("byte to string exception~~~");
        }
        // 记录报文流水信息
        CMT384Bean bean = (CMT384Bean) beanBody.getMessageBean();
        tChnCollectSingleLogDAO.saveRealCollectLog(collectionChargesBean, bean.getMsgId(), beanHead.getComRefId());
        logger.info("[saveRealCollectLog successful]");
        // 发送报文
        MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMT384);
        messageSend.sendMessage(messageBean);
        logger.info("[sendMessage successful]");
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
        MessageHeaderBean beanHead = MsgHeadAss.commMsgHeaderReq(MessageTypeEnum.CMT386.value());
        MessageBean beanBody = RealTimePayAss.realtimePayMsgBodyReq(paymentBean);
        byte[] message;
        try {
            message = messageAssemble.assemble(beanHead, beanBody);
        }
        catch (HZRealTransferException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        // 记录报文流水信息
        CMT386Bean bean = (CMT386Bean) beanBody.getMessageBean();
        tChnPaymentSingleLogDAO.saveRealPaymentLog(paymentBean, bean.getMsgId(), beanHead.getComRefId());
        MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMT384);
        messageSend.sendMessage(messageBean);
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean queryTrade(String txnseqno) {
        TTxnsLogDO txnsLogDO = txnsLogDAO.getTxnsLogByTxnseqno(txnseqno);
        String businessType = txnsLogDO.getBusitype();
        // 查询原交易获取原报文三要素
        OrgnlTxBean orgMsgIde = null;
        if (BusinessType.REAL_TIME_COLL.getValue().equals(businessType)) {
            // 实时代收原交易
            TChnCollectSingleLogDO collSingle = tChnCollectSingleLogDAO.getCollSingleByTxnseqno(txnseqno);
            if (collSingle == null) {
                logger.error("cann't find record by txnseqno : " + txnseqno);
                return new ResultBean(ErrorCodeHZ.NONE_RECORD.getValue(), ErrorCodeHZ.NONE_RECORD.getDisplayName());
            }
            orgMsgIde = new OrgnlTxBean(OrgCode.ZCBS.getValue(), collSingle.getTxid(), MessageTypeEnum.CMT384.value());
        }
        else if (BusinessType.REAL_TIME_PAY.getValue().equals(businessType)) {
            // 实时代付原交易
            TChnPaymentSingleLogDO paySingle = tChnPaymentSingleLogDAO.getPaySingleByTxnseqno(txnseqno);
            if (paySingle == null) {
                logger.error("cann't find record by txnseqno : " + txnseqno);
                return new ResultBean(ErrorCodeHZ.NONE_RECORD.getValue(), ErrorCodeHZ.NONE_RECORD.getDisplayName());
            }
            orgMsgIde = new OrgnlTxBean(OrgCode.ZCBS.getValue(), paySingle.getTxid(), MessageTypeEnum.CMT386.value());
        }
        // CMS316报文组装
        MessageHeaderBean beanHead = MsgHeadAss.commMsgHeaderReq(MessageTypeEnum.CMS316.value());
        MessageBean beanBody = BusStatQryAss.busStatusQryMsgBodyReq(orgMsgIde);
        byte[] message;
        try {
            message = messageAssemble.assemble(beanHead, beanBody);
        }
        catch (HZRealTransferException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        // 记录报文流水信息
        MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMS316);
        messageSend.sendMessage(messageBean);
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean check() {
        // CMS991报文组装
        MessageHeaderBean beanHead = MsgHeadAss.commMsgHeaderReq(MessageTypeEnum.CMS991.value());
        MessageBean beanBody = ComuDetecAss.communicateDetecMsgBodyReq();
        byte[] message;
        try {
            message = messageAssemble.assemble(beanHead, beanBody);
        }
        catch (HZRealTransferException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        // 发送报文
        MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMS991);
        messageSend.sendMessage(messageBean);
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

}
