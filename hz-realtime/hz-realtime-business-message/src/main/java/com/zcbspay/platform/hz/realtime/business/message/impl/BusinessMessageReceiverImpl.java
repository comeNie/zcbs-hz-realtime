package com.zcbspay.platform.hz.realtime.business.message.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.business.message.bean.TransLogUpBean;
import com.zcbspay.platform.hz.realtime.business.message.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TChnCollectSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TxnsLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.enums.BusinessType;
import com.zcbspay.platform.hz.realtime.business.message.enums.HZRspCode;
import com.zcbspay.platform.hz.realtime.business.message.enums.HZRspStatus;
import com.zcbspay.platform.hz.realtime.business.message.enums.RealCPOrdSts;
import com.zcbspay.platform.hz.realtime.business.message.enums.ReturnInfo;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TChnPaymentSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.service.BusinessMessageReceiver;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.MessageRespBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.business.message.service.enums.ErrorCodeBusHZ;
import com.zcbspay.platform.hz.realtime.message.bean.CMS317Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS911Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT385Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT387Bean;
import com.zcbspay.platform.hz.realtime.message.bean.RspnInfBean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;

@Service("businessMessageReceiver")
public class BusinessMessageReceiverImpl implements BusinessMessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(BusinessMessageReceiverImpl.class);

    @Autowired
    private TChnCollectSingleLogDAO tChnCollectSingleLogDAO;
    @Autowired
    private TChnPaymentSingleLogDAO tChnPaymentSingleLogDAO;
    @Autowired
    private OrderCollectSingleDAO orderCollectSingleDAO;
    @Autowired
    private OrderPaymentSingleDAO orderPaymentSingleDAO;
    @Autowired
    private TxnsLogDAO txnsLogDAO;

    @Override
    public ResultBean realTimeCollectionChargesReceipt(MessageRespBean messageRespBean) {
        CMT385Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMT385Bean.class);
        String msgId = bean.getOrgnlMsgId().getOrgnlMsgId();
        // 回执判重
        TChnCollectSingleLogDO record = tChnCollectSingleLogDAO.getCollSingleByMsgId(msgId);
        if (record != null) {
            if (bean.getMsgId().equals(record.getRspmsgid()) || (StringUtils.isNotEmpty(record.getRspstatus()) && HZRspStatus.parseOf(record.getRspstatus()) != null)) {
                logger.error("【repeat response and msgId is】 : " + record.getRspmsgid());
                return new ResultBean(ErrorCodeBusHZ.REPEAT_RESP.getValue(), ErrorCodeBusHZ.REPEAT_RESP.getDisplayName());
            }
        }
        // 更新流水记录
        TChnCollectSingleLogDO chnCollectSingleLogDO = tChnCollectSingleLogDAO.updateRealCollectLog(bean);
        // 更新主流水记录
        RspnInfBean respBean = bean.getRspnInf();
        updateTxnsLogInfo(BusinessType.REAL_TIME_COLL, respBean.getRjctcd(), chnCollectSingleLogDO.getTxnseqno());
        // 更新订单状态
        String status = HZRspCode.SUCCESS.getValue().equals(bean.getRspnInf().getSts()) ? RealCPOrdSts.SCUCESS.getValue() : RealCPOrdSts.FAILED.getValue();
        int effRow = orderCollectSingleDAO.updateOrderStatus(chnCollectSingleLogDO.getTxnseqno(), status);
        if (effRow == 0) {
            logger.error("【 no collection single record to update!!!】");
            return new ResultBean(ErrorCodeBusHZ.NONE_PAY_ORDER.getValue(), ErrorCodeBusHZ.NONE_PAY_ORDER.getDisplayName());
        }
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean realTimePaymentReceipt(MessageRespBean messageRespBean) {
        CMT387Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMT387Bean.class);
        String msgId = bean.getOrgnlMsgId().getOrgnlMsgId();
        // 回执判重
        TChnPaymentSingleLogDO record = tChnPaymentSingleLogDAO.getPaySingleByMsgId(msgId);
        if (record != null) {
            if (bean.getMsgId().equals(record.getRspmsgid()) || (StringUtils.isNotEmpty(record.getRspstatus()) && HZRspStatus.parseOf(record.getRspstatus()) != null)) {
                logger.error("【repeat response and msgId is】 : " + record.getRspmsgid());
                return new ResultBean(ErrorCodeBusHZ.REPEAT_RESP.getValue(), ErrorCodeBusHZ.REPEAT_RESP.getDisplayName());
            }
        }
        // 更新流水记录
        TChnPaymentSingleLogDO chnPaymentSingleLogDO = tChnPaymentSingleLogDAO.updateRealPaymentLog(bean);
        // 更新主流水记录
        RspnInfBean respBean = bean.getRspnInf();
        updateTxnsLogInfo(BusinessType.REAL_TIME_PAY, respBean.getRjctcd(), chnPaymentSingleLogDO.getTxnseqno());
        // 更新订单状态
        String status = HZRspCode.SUCCESS.getValue().equals(bean.getRspnInf().getSts()) ? RealCPOrdSts.SCUCESS.getValue() : RealCPOrdSts.FAILED.getValue();
        int effRow = orderPaymentSingleDAO.updateOrderStatus(chnPaymentSingleLogDO.getTxnseqno(), status);
        if (effRow == 0) {
            logger.error("【 no payment single record to update!!!】");
            return new ResultBean(ErrorCodeBusHZ.NONE_PAY_ORDER.getValue(), ErrorCodeBusHZ.NONE_PAY_ORDER.getDisplayName());
        }
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean discardMessage(MessageRespBean messageRespBean) {

        CMS911Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMS911Bean.class);
        String orgMsgType = bean.getDscrdInf().getMsgType();
        logger.error("【" + orgMsgType + "】 message is discard by Hangzhou Clearing Center!!!");
        if (MessageTypeEnum.CMT384.value().equals(orgMsgType)) {
            tChnCollectSingleLogDAO.updateRealCollectLogDiscard(bean);
        }
        else if (MessageTypeEnum.CMT386.value().equals(orgMsgType)) {
            tChnPaymentSingleLogDAO.updateRealPaymentLogDiscard(bean);
        }
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean busStaQryResp(MessageRespBean messageRespBean) {
        ResultBean resultBean = null;
        CMS317Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMS317Bean.class);
        String orgnlTxId = bean.getOrgnlTx().getOrgnlTxId();

        TChnCollectSingleLogDO collDo = tChnCollectSingleLogDAO.getCollSingleByTxId(orgnlTxId);
        if (collDo == null) {
            TChnPaymentSingleLogDO payDo = tChnPaymentSingleLogDAO.getCollSingleByTxId(orgnlTxId);
            if (payDo != null) {
                // 回执判重
                if (bean.getMsgId().equals(payDo.getRspmsgid()) || (StringUtils.isNotEmpty(payDo.getRspstatus()) && HZRspStatus.parseOf(payDo.getRspstatus()) != null)) {
                    logger.error("【repeat response and msgId is】 : " + payDo.getRspmsgid());
                    return new ResultBean(ErrorCodeBusHZ.REPEAT_RESP.getValue(), ErrorCodeBusHZ.REPEAT_RESP.getDisplayName());
                }
                // 更新流水记录
                TChnPaymentSingleLogDO chnPaymentSingleLogDO = tChnPaymentSingleLogDAO.updateRealPaymentLog(bean, payDo.getTid());
                // 更新主流水记录
                RspnInfBean respBean = bean.getRspnInf();
                updateTxnsLogInfo(BusinessType.REAL_TIME_PAY, respBean.getRjctcd(), chnPaymentSingleLogDO.getTxnseqno());
                // 更新订单状态
                String status = HZRspCode.SUCCESS.getValue().equals(bean.getRspnInf().getSts()) ? RealCPOrdSts.SCUCESS.getValue() : RealCPOrdSts.FAILED.getValue();
                int effRow = orderPaymentSingleDAO.updateOrderStatus(chnPaymentSingleLogDO.getTxnseqno(), status);
                if (effRow == 0) {
                    logger.error("【 no payment single record to update!!!】");
                    return new ResultBean(ErrorCodeBusHZ.NONE_PAY_ORDER.getValue(), ErrorCodeBusHZ.NONE_PAY_ORDER.getDisplayName());
                }
                resultBean = new ResultBean(ReturnInfo.SUCCESS.getValue());
            }
            else {
                logger.error("【no orignal trade record !!!】  ");
            }
        }
        else {
            // 回执判重
            if (bean.getMsgId().equals(collDo.getRspmsgid()) || (StringUtils.isNotEmpty(collDo.getRspstatus()) && HZRspStatus.parseOf(collDo.getRspstatus()) != null)) {
                logger.error("【repeat response and msgId is】 : " + collDo.getRspmsgid());
                return new ResultBean(ErrorCodeBusHZ.REPEAT_RESP.getValue(), ErrorCodeBusHZ.REPEAT_RESP.getDisplayName());
            }
            // 更新流水记录
            TChnCollectSingleLogDO chnCollectSingleLogDAO = tChnCollectSingleLogDAO.updateRealCollectLog(bean, collDo.getTid());
            // 更新主流水记录
            RspnInfBean respBean = bean.getRspnInf();
            updateTxnsLogInfo(BusinessType.REAL_TIME_PAY, respBean.getRjctcd(), chnCollectSingleLogDAO.getTxnseqno());
            // 更新订单状态
            String status = HZRspCode.SUCCESS.getValue().equals(bean.getRspnInf().getSts()) ? RealCPOrdSts.SCUCESS.getValue() : RealCPOrdSts.FAILED.getValue();
            int effRow = orderCollectSingleDAO.updateOrderStatus(chnCollectSingleLogDAO.getTxnseqno(), status);
            if (effRow == 0) {
                logger.error("【 no payment single record to update!!!】");
                return new ResultBean(ErrorCodeBusHZ.NONE_PAY_ORDER.getValue(), ErrorCodeBusHZ.NONE_PAY_ORDER.getDisplayName());
            }
            resultBean = new ResultBean(ReturnInfo.SUCCESS.getValue());
        }

        return resultBean;
    }

    @Override
    public ResultBean commProcAfrmResp(MessageRespBean messageRespBean) {
        CMS900Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMS900Bean.class);
        String orgMsgType = bean.getOrgnlMsgId().getOgnlMsgType();
        if (MessageTypeEnum.CMT384.value().equals(orgMsgType)) {
            tChnCollectSingleLogDAO.updateRealCollectLogCommResp(bean);
        }
        else if (MessageTypeEnum.CMT386.value().equals(orgMsgType)) {
            tChnPaymentSingleLogDAO.updateRealPaymentLogCommResp(bean);
        }
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    /**
     * 更新主流水信息
     * 
     * @param businessType
     * @param retCode
     * @param txnSeq
     */
    private void updateTxnsLogInfo(BusinessType businessType, String retCode, String txnSeq) {
        TransLogUpBean transLogUpBean = new TransLogUpBean();
        transLogUpBean.setBusinessType(businessType);
        transLogUpBean.setTxnseqno(txnSeq);
        transLogUpBean.setPayretcode(retCode);
        txnsLogDAO.updatePayInfoResult(transLogUpBean);
    }

}
