package com.zcbspay.platform.hz.realtime.business.message.impl;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.zcbspay.platform.hz.realtime.business.message.assembly.BusStatQryAss;
import com.zcbspay.platform.hz.realtime.business.message.assembly.ComuDetecAss;
import com.zcbspay.platform.hz.realtime.business.message.assembly.MsgHeadAss;
import com.zcbspay.platform.hz.realtime.business.message.assembly.RealTimeCollAss;
import com.zcbspay.platform.hz.realtime.business.message.assembly.RealTimePayAss;
import com.zcbspay.platform.hz.realtime.business.message.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TChnCollectSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TxnsLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.enums.BusinessType;
import com.zcbspay.platform.hz.realtime.business.message.enums.HZRspStatus;
import com.zcbspay.platform.hz.realtime.business.message.enums.OrgCode;
import com.zcbspay.platform.hz.realtime.business.message.enums.RealCPOrdSts;
import com.zcbspay.platform.hz.realtime.business.message.enums.TradeStatFlagEnum;
import com.zcbspay.platform.hz.realtime.business.message.pojo.ChnCollectSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.ChnPaymentSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.OrderPaymentSingleDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TxnsLogDO;
import com.zcbspay.platform.hz.realtime.business.message.service.BusinesssMessageSender;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.BusinessRsltBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.TChnCollectSingleLogVO;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.TChnPaymentSingleLogVO;
import com.zcbspay.platform.hz.realtime.business.message.service.enums.ErrorCodeBusHZ;
import com.zcbspay.platform.hz.realtime.business.message.service.exception.HZRealBusException;
import com.zcbspay.platform.hz.realtime.message.bean.CMT384Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT386Bean;
import com.zcbspay.platform.hz.realtime.message.bean.OrgnlTxBean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.MessageSend;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBeanStr;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.exception.HZRealFeException;
import com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageAssemble;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.exception.HZRealTransferException;
import com.zcbspay.platform.member.merchant.service.MerchService;

@Service("businesssMessageSender")
public class BusinesssMessageSenderImpl implements BusinesssMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(BusinesssMessageSenderImpl.class);

    @com.alibaba.dubbo.config.annotation.Reference(version = "1.0")
    private MessageSend messageSend;
    @com.alibaba.dubbo.config.annotation.Reference(version = "1.0")
    private MessageAssemble messageAssemble;
    @com.alibaba.dubbo.config.annotation.Reference(version = "1.0")
    private MerchService merchService;
    @Autowired
    private TChnCollectSingleLogDAO tChnCollectSingleLogDAO;
    @Autowired
    private TChnPaymentSingleLogDAO tChnPaymentSingleLogDAO;
    @Autowired
    private TxnsLogDAO txnsLogDAO;
    @Autowired
    private OrderCollectSingleDAO orderCollectSingleDAO;
    @Autowired
    private OrderPaymentSingleDAO orderPaymentSingleDAO;

    @Override
    public ResultBean realTimeCollectionCharges(SingleCollectionChargesBean collectionChargesBean) {
        ResultBean resultBean = null;
        try {
            // 业务规则校验
            String merOrgCode = businessCheckColl(collectionChargesBean.getTxnseqno());
            String senderOrgCode = getSenderOrgCodeByMerOrgCode(merOrgCode);
            // CMT384报文组装
            MessageHeaderBean beanHead = MsgHeadAss.commMsgHeaderReq(MessageTypeEnum.CMT384.value(), senderOrgCode);
            logger.info("[beanHead is]:" + beanHead);
            MessageBean beanBody = RealTimeCollAss.realtimeCollMsgBodyReq(collectionChargesBean);
            logger.info("[beanBody is]:" + beanBody);
            byte[] message = messageAssemble.assemble(beanHead, beanBody);
            logger.info("[assembled message is]:" + new String(message, "utf-8"));
            // 记录报文流水信息
            CMT384Bean bean = (CMT384Bean) beanBody.getMessageBean();
            ChnCollectSingleLogDO collDo = tChnCollectSingleLogDAO.saveRealCollectLog(collectionChargesBean, bean.getMsgId(), beanHead.getComRefId(), senderOrgCode);
            logger.info("[saveRealCollectLog successful]");
            // 更新交易流水支付信息
            txnsLogDAO.updatePayInfo(collDo.getTxnseqno(), collDo.getTxid(), senderOrgCode);
            // 发送报文
            MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMT384);
            com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.ResultBean resSendMsg = messageSend.sendMessage(messageBean);
            if (resSendMsg.isResultBool()) {
                logger.info("[sendMessage successful]");
            }
            else {
                // 更新渠道流水状态为失败
                tChnCollectSingleLogDAO.updateRealCollLogSendInfo(collDo.getTid(), HZRspStatus.REJECTED.getValue(), resSendMsg.getErrCode() + resSendMsg.getErrMsg());
                resultBean = new ResultBean();
                BeanUtils.copyProperties(resSendMsg, resultBean);
                return resultBean;
            }
            // 轮训查询通用应答结果
            resultBean = cycleQuerySynRsp(collDo.getMsgid(), collDo.getTxnseqno(), MessageTypeEnum.CMT384.value(), collDo.getTid());
        }
        catch (HZRealTransferException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        catch (UnsupportedEncodingException e) {
            logger.error("byte to string exception~~~");
        }
        catch (HZRealFeException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            resultBean = new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        catch (HZRealBusException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        return resultBean;
    }

    private String businessCheckColl(String txnseqno) throws HZRealBusException {
        // 商户在中创标石开立的机构号
        String merOrgCode = null;
        // 主流水校验
        checkTnxLog(txnseqno);
        // 代收订单状态校验
        OrderCollectSingleDO order = orderCollectSingleDAO.getCollSingOrdByTxnseqno(txnseqno);
        if (order == null) {
            logger.error("【 no collection single order record to update!!!】" + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.NONE_PAY_ORDER.getValue(), ErrorCodeBusHZ.NONE_PAY_ORDER.getDisplayName());
        }
        if (!RealCPOrdSts.INITIAL.getValue().equals(order.getStatus()) && !RealCPOrdSts.FAILED.getValue().equals(order.getStatus())) {
            logger.error("【OrderCollectSingleDO status is wrong and it's rejected!!!】" + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.ORDER_STS_WRONG.getValue(), ErrorCodeBusHZ.ORDER_STS_WRONG.getDisplayName());
        }
        // 交易判重
        ChnCollectSingleLogDO record = tChnCollectSingleLogDAO.getCollSingleByTxnseqnoNotFail(txnseqno);
        if (record != null) {
            logger.error("repeat request and txnseqno is : " + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.REPEAT_REQUEST.getValue(), ErrorCodeBusHZ.REPEAT_REQUEST.getDisplayName());
        }
        merOrgCode = order.getMerid();
        return merOrgCode;
    }

    private String businessCheckPay(String txnseqno) throws HZRealBusException {
        // 主流水校验
        checkTnxLog(txnseqno);
        // 代付订单状态校验
        OrderPaymentSingleDO order = orderPaymentSingleDAO.getPaySingOrdByTxnseqno(txnseqno);
        if (order == null) {
            logger.error("【 no collection single order record to update!!!】" + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.NONE_PAY_ORDER.getValue(), ErrorCodeBusHZ.NONE_PAY_ORDER.getDisplayName());
        }
        if (!RealCPOrdSts.INITIAL.getValue().equals(order.getStatus()) && !RealCPOrdSts.FAILED.getValue().equals(order.getStatus())) {
            logger.error("【OrderCollectSingleDO status is wrong and it's rejected!!!】" + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.ORDER_STS_WRONG.getValue(), ErrorCodeBusHZ.ORDER_STS_WRONG.getDisplayName());
        }
        // 交易判重
        ChnPaymentSingleLogDO record = tChnPaymentSingleLogDAO.getPaySingleByTxnseqnoNotFail(txnseqno);
        if (record != null) {
            logger.error("repeat request and txnseqno is : " + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.REPEAT_REQUEST.getValue(), ErrorCodeBusHZ.REPEAT_REQUEST.getDisplayName());
        }
        return order.getMerid();
    }

    private void checkTnxLog(String txnseqno) throws HZRealBusException {
        // 主流水检测
        TxnsLogDO txnsLogDO = txnsLogDAO.getTxnsLogByTxnseqno(txnseqno);
        if (txnsLogDO == null) {
            logger.error("【 no collection single log record to update!!!】");
            throw new HZRealBusException(ErrorCodeBusHZ.NONE_PAY_LOG.getValue(), ErrorCodeBusHZ.NONE_PAY_LOG.getDisplayName());
        }
        if (!TradeStatFlagEnum.PAYING.getStatus().equals(txnsLogDO.getTradestatflag())) {
            logger.error("【TTxnsLogDO status is wrong and it's rejected!!!】" + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.CHL_SER_STS_WR.getValue(), ErrorCodeBusHZ.CHL_SER_STS_WR.getDisplayName());
        }
    }

    @Override
    public ResultBean realTimePayment(SinglePaymentBean paymentBean) {
        ResultBean resultBean = null;
        try {
            // 业务规则校验
            String merOrgCode = businessCheckPay(paymentBean.getTxnseqno());
            String senderOrgCode = getSenderOrgCodeByMerOrgCode(merOrgCode);
            // CMT386报文组装
            MessageHeaderBean beanHead = MsgHeadAss.commMsgHeaderReq(MessageTypeEnum.CMT386.value(), senderOrgCode);
            MessageBean beanBody = RealTimePayAss.realtimePayMsgBodyReq(paymentBean);
            byte[] message = messageAssemble.assemble(beanHead, beanBody);
            // 记录报文流水信息
            CMT386Bean bean = (CMT386Bean) beanBody.getMessageBean();
            ChnPaymentSingleLogDO payDo = tChnPaymentSingleLogDAO.saveRealPaymentLog(paymentBean, bean.getMsgId(), beanHead.getComRefId(), senderOrgCode);
            // 更新交易流水支付信息
            txnsLogDAO.updatePayInfo(payDo.getTxnseqno(), payDo.getTxid(), senderOrgCode);
            MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMT386);
            com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.ResultBean resSendMsg = messageSend.sendMessage(messageBean);
            if (resSendMsg.isResultBool()) {
                logger.info("[sendMessage successful]");
            }
            else {
                // 更新渠道流水状态为失败
                tChnPaymentSingleLogDAO.updateRealPayLogSendInfo(payDo.getTid(), HZRspStatus.REJECTED.getValue(), resSendMsg.getErrCode() + resSendMsg.getErrMsg());
                resultBean = new ResultBean();
                BeanUtils.copyProperties(resSendMsg, resultBean);
                return resultBean;
            }
            // 轮训查询通用应答结果
            resultBean = cycleQuerySynRsp(payDo.getMsgid(), payDo.getTxnseqno(), MessageTypeEnum.CMT386.value(), payDo.getTid());
        }
        catch (HZRealTransferException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        catch (HZRealFeException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            resultBean = new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        catch (HZRealBusException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        return resultBean;
    }

    @Override
    public ResultBean queryTrade(String txnseqno) {
        ResultBean resultBean = null;
        TxnsLogDO txnsLogDO = txnsLogDAO.getTxnsLogByTxnseqno(txnseqno);
        String businessType = null;
        if (txnsLogDO != null) {
            businessType = txnsLogDO.getBusicode();
        }
        else {
            logger.error("【cann't find TxnsLog record by txnseqno】: " + txnseqno);
            return new ResultBean(ErrorCodeBusHZ.NONE_MAIN_REC.getValue(), ErrorCodeBusHZ.NONE_MAIN_REC.getDisplayName());
        }
        try {
            // 查询原交易获取原报文三要素
            OrgnlTxBean orgMsgIde = null;
            if (BusinessType.REAL_TIME_COLL.getValue().equals(businessType)) {
                // 实时代收原交易
                ChnCollectSingleLogDO collSingle = tChnCollectSingleLogDAO.getCollSingleByTxnseqnoAndRspSta(txnseqno, HZRspStatus.UNKNOWN.getValue(), HZRspStatus.SUCCESS.getValue());
                if (collSingle == null) {
                    logger.error("cann't find record by txnseqno : " + txnseqno);
                    return new ResultBean(ErrorCodeBusHZ.NONE_RECORD.getValue(), ErrorCodeBusHZ.NONE_RECORD.getDisplayName());
                }
                if (!HZRspStatus.UNKNOWN.getValue().equals(collSingle.getRspstatus())) {
                    // 原交易已收到业务应答
                    TChnCollectSingleLogVO vo = new TChnCollectSingleLogVO();
                    BeanUtils.copyProperties(collSingle, vo);
                    return new ResultBean(vo);
                }
                else {
                    String merOrgCode = orderCollectSingleDAO.getCollSingOrdByTxnseqno(txnseqno).getMerid();
                    String senderOrgCode = getSenderOrgCodeByMerOrgCode(merOrgCode);
                    // 发送CMS316报文同步查询信息
                    orgMsgIde = new OrgnlTxBean(senderOrgCode, collSingle.getTxid(), MessageTypeEnum.CMT384.value());
                    // CMS316报文组装
                    MessageHeaderBean beanHead = MsgHeadAss.commMsgHeaderReq(MessageTypeEnum.CMS316.value(), senderOrgCode);
                    MessageBean beanBody = BusStatQryAss.busStatusQryMsgBodyReq(orgMsgIde);
                    byte[] message = messageAssemble.assemble(beanHead, beanBody);
                    MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMS316);
                    com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.ResultBean resSendMsg = messageSend.sendMessage(messageBean);
                    if (resSendMsg.isResultBool()) {
                        logger.info("[sendMessage successful]");
                    }
                    else {
                        resultBean = new ResultBean();
                        BeanUtils.copyProperties(resSendMsg, resultBean);
                        return resultBean;
                    }
                    // 轮训查询结果
                    resultBean = cycleQueryBusRsp(collSingle.getMsgid(), txnseqno, MessageTypeEnum.CMT384.value(), collSingle.getTid());
                    TChnCollectSingleLogVO vo = new TChnCollectSingleLogVO();
                    BeanUtils.copyProperties(collSingle, vo);
                    return resultBean == null ? new ResultBean(vo) : resultBean;
                }
            }
            else if (BusinessType.REAL_TIME_PAY.getValue().equals(businessType)) {
                // 实时代付原交易
                ChnPaymentSingleLogDO paySingle = tChnPaymentSingleLogDAO.getPaySingleByTxnseqnoAndRspSta(txnseqno, HZRspStatus.UNKNOWN.getValue(), HZRspStatus.SUCCESS.getValue());
                if (paySingle == null) {
                    logger.error("cann't find record by txnseqno : " + txnseqno);
                    return new ResultBean(ErrorCodeBusHZ.NONE_RECORD.getValue(), ErrorCodeBusHZ.NONE_RECORD.getDisplayName());
                }
                if (!HZRspStatus.UNKNOWN.getValue().equals(paySingle.getRspstatus())) {
                    // 原交易已收到业务应答
                    TChnCollectSingleLogVO vo = new TChnCollectSingleLogVO();
                    BeanUtils.copyProperties(paySingle, vo);
                    return new ResultBean(vo);
                }
                else {
                    String merOrgCode = orderPaymentSingleDAO.getPaySingOrdByTxnseqno(txnseqno).getMerid();
                    String senderOrgCode = getSenderOrgCodeByMerOrgCode(merOrgCode);
                    // 发送CMS316报文同步查询信息
                    orgMsgIde = new OrgnlTxBean(senderOrgCode, paySingle.getTxid(), MessageTypeEnum.CMT386.value());
                    // CMS316报文组装
                    MessageHeaderBean beanHead = MsgHeadAss.commMsgHeaderReq(MessageTypeEnum.CMS316.value(), senderOrgCode);
                    MessageBean beanBody = BusStatQryAss.busStatusQryMsgBodyReq(orgMsgIde);
                    byte[] message = messageAssemble.assemble(beanHead, beanBody);
                    MessageBeanStr messageBean = new MessageBeanStr(message, MessageTypeEnum.CMS316);
                    com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.ResultBean resSendMsg = messageSend.sendMessage(messageBean);
                    if (resSendMsg.isResultBool()) {
                        logger.info("[sendMessage successful]");
                    }
                    else {
                        resultBean = new ResultBean();
                        BeanUtils.copyProperties(resSendMsg, resultBean);
                        return resultBean;
                    }
                    // 轮训查询结果
                    resultBean = cycleQueryBusRsp(paySingle.getMsgid(), txnseqno, MessageTypeEnum.CMT386.value(), paySingle.getTid());
                    TChnPaymentSingleLogVO vo = new TChnPaymentSingleLogVO();
                    BeanUtils.copyProperties(paySingle, vo);
                    return resultBean == null ? new ResultBean(vo) : resultBean;
                }
            }
            else {
                logger.error("【unknown businessType】: " + businessType);
                return new ResultBean(ErrorCodeBusHZ.UNKNOWN_BT.getValue(), ErrorCodeBusHZ.UNKNOWN_BT.getDisplayName());
            }
        }
        catch (HZRealTransferException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        catch (HZRealFeException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            resultBean = new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        return resultBean;
    }

    @Override
    public ResultBean check() {
        ResultBean resultBean = null;
        String detectRspMsg = null;
        // CMS991报文组装
        MessageHeaderBean beanHead = MsgHeadAss.commMsgHeaderReq(MessageTypeEnum.CMS991.value(), OrgCode.DEFAUT.getValue());
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
        try {
            com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.ResultBean resSendMsg = messageSend.sendMessage(messageBean);
            if (resSendMsg.isResultBool()) {
                detectRspMsg = (String) resSendMsg.getResultObj();
                logger.info("【detectRspMsg is 】:" + detectRspMsg);
            }
            else {
                resultBean = new ResultBean();
                BeanUtils.copyProperties(resSendMsg, resultBean);
                return resultBean;
            }
            if (StringUtils.isEmpty(detectRspMsg)) {
                resultBean = new ResultBean(ErrorCodeBusHZ.CONN_FAIL.getValue(), ErrorCodeBusHZ.CONN_FAIL.getDisplayName());
            }
            else {
                resultBean = new ResultBean(detectRspMsg);
            }
        }
        catch (HZRealFeException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            resultBean = new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        return resultBean;
    }

    /**
     * 轮训查询同步应答结果，时间以2的幂次递增，不超过40s
     * 
     * @param mye
     * 
     * @param orderId
     * @param transTm
     * @return
     * @throws UnionPayException
     */
    private ResultBean cycleQuerySynRsp(String msgid, String txnseqno, String msgType, long tid) throws HZRealTransferException {
        ResultBean resultBean = null;
        BusinessRsltBean businessRsltBean = null;
        ChnCollectSingleLogDO resultColl = null;
        ChnPaymentSingleLogDO resultPay = null;
        String status = null;
        int time = 2000;
        int cycTimes = 1;
        try {
            do {
                TimeUnit.MILLISECONDS.sleep(time);
                if (MessageTypeEnum.CMT384.value().equals(msgType)) {
                    resultColl = tChnCollectSingleLogDAO.getCollSingleByTid(tid);
                    status = resultColl.getComstatus();
                }
                if (MessageTypeEnum.CMT386.value().equals(msgType)) {
                    resultPay = tChnPaymentSingleLogDAO.getPaySingleByTid(tid);
                    status = resultPay.getComstatus();
                }
                time = 2 * time;
                if (++cycTimes > 4) {
                    break;
                }
            } while (StringUtils.isEmpty(status));
            logger.info("【finish loop query~】");
        }
        catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new HZRealTransferException(ErrorCodeBusHZ.INTERRUPT_EXP.getValue(), ErrorCodeBusHZ.INTERRUPT_EXP.getDisplayName());
        }
        if (!StringUtils.isEmpty(status)) {
            businessRsltBean = new BusinessRsltBean();
            businessRsltBean.setMsgid(msgid);
            businessRsltBean.setTxnseqno(txnseqno);
            if (MessageTypeEnum.CMT384.value().equals(msgType)) {
                businessRsltBean.setCommRspSts(resultColl.getComstatus());
                businessRsltBean.setCommRspCode(resultColl.getComrejectcode());
                businessRsltBean.setCommRspInfo(resultColl.getComrejectinformation());
            }
            if (MessageTypeEnum.CMT386.value().equals(msgType)) {
                businessRsltBean.setCommRspSts(resultPay.getComstatus());
                businessRsltBean.setCommRspCode(resultPay.getComrejectcode());
                businessRsltBean.setCommRspInfo(resultPay.getComrejectinformation());
            }
            resultBean = new ResultBean(businessRsltBean);
        }
        return resultBean;
    }

    /**
     * 轮训查询异步业务应答结果，时间以2的幂次递增，不超过40s
     * 
     * @param mye
     * 
     * @param orderId
     * @param transTm
     * @return
     * @throws UnionPayException
     */
    private ResultBean cycleQueryBusRsp(String msgid, String txnseqno, String msgType, long tid) throws HZRealTransferException {
        ResultBean resultBean = null;
        ChnCollectSingleLogDO resultColl = null;
        ChnPaymentSingleLogDO resultPay = null;
        String status = null;
        int time = 2000;
        int cycTimes = 1;
        try {
            do {
                TimeUnit.MILLISECONDS.sleep(time);
                if (MessageTypeEnum.CMT384.value().equals(msgType)) {
                    resultColl = tChnCollectSingleLogDAO.getCollSingleByTid(tid);
                    status = resultColl.getRspstatus();
                }
                if (MessageTypeEnum.CMT386.value().equals(msgType)) {
                    resultPay = tChnPaymentSingleLogDAO.getPaySingleByTid(tid);
                    status = resultPay.getRspstatus();
                }
                time = 2 * time;
                if (++cycTimes > 4) {
                    break;
                }
            } while (HZRspStatus.UNKNOWN.getValue().equals(status));
            logger.info("【finish loop query~】");
        }
        catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new HZRealTransferException(ErrorCodeBusHZ.INTERRUPT_EXP.getValue(), ErrorCodeBusHZ.INTERRUPT_EXP.getDisplayName());
        }
        if (!HZRspStatus.UNKNOWN.getValue().equals(status)) {
            if (MessageTypeEnum.CMT384.value().equals(msgType)) {
                TChnCollectSingleLogVO vo = new TChnCollectSingleLogVO();
                BeanUtils.copyProperties(resultColl, vo);
                resultBean = new ResultBean(vo);
            }
            if (MessageTypeEnum.CMT386.value().equals(msgType)) {
                TChnPaymentSingleLogVO vo = new TChnPaymentSingleLogVO();
                BeanUtils.copyProperties(resultPay, vo);
                resultBean = new ResultBean(vo);
            }
        }
        return resultBean;
    }

    /**
     * 通过商户机构号获取宁波清算中心发送机构号
     * 
     * @return
     */
    private String getSenderOrgCodeByMerOrgCode(String memberId) {
        // TODO 测试不调用member，生产环境打开注释
        return merchService.getMerchBymemberId(memberId).getOrgcode();
        // return OrgCode.DEFAUT.getValue();
    }
}
