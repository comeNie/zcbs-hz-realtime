package com.zcbspay.platform.hz.realtime.business.message.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.business.message.bean.PayerAndPayeeBean;
import com.zcbspay.platform.hz.realtime.business.message.bean.TransLogUpBean;
import com.zcbspay.platform.hz.realtime.business.message.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TChnCollectSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TxnsLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.enums.BusinessType;
import com.zcbspay.platform.hz.realtime.business.message.enums.HZRspStatus;
import com.zcbspay.platform.hz.realtime.business.message.enums.RealCPOrdSts;
import com.zcbspay.platform.hz.realtime.business.message.enums.ReturnInfo;
import com.zcbspay.platform.hz.realtime.business.message.enums.TradeStatFlagEnum;
import com.zcbspay.platform.hz.realtime.business.message.pojo.ChnCollectSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.ChnPaymentSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.service.BusinessMessageReceiver;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.MessageRespBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.business.message.service.enums.ErrorCodeBusHZ;
import com.zcbspay.platform.hz.realtime.business.message.service.exception.HZRealBusException;
import com.zcbspay.platform.hz.realtime.message.bean.BusiTextBean;
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
        ChnCollectSingleLogDO record = tChnCollectSingleLogDAO.getCollSingleByMsgId(msgId);
        if (record != null) {
            String status = record.getRspstatus();
            if (bean.getMsgId().equals(record.getRspmsgid()) || !HZRspStatus.UNKNOWN.getValue().equals(status)) {
                logger.error("【repeat response and msgId is】 : " + record.getRspmsgid());
                return new ResultBean(ErrorCodeBusHZ.REPEAT_RESP.getValue(), ErrorCodeBusHZ.REPEAT_RESP.getDisplayName());
            }
        }
        else {
            logger.error("【no orignal trade record !!!】  ");
        }
        // 关键信息匹配
        try {
            PayerAndPayeeBean payAndPayBean = new PayerAndPayeeBean();
            BeanUtils.copyProperties(record, payAndPayBean);
            mainInfoMatchingCheck(bean.getBusiText(), payAndPayBean, null);
        }
        catch (HZRealBusException e) {
            logger.error(e.getErrCode() + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        // 更新流水记录
        ChnCollectSingleLogDO chnCollectSingleLogDO = tChnCollectSingleLogDAO.updateRealCollectLog(bean);
        // 更新主流水记录
        RspnInfBean respBean = bean.getRspnInf();
        updateTxnsLogInfo(BusinessType.REAL_TIME_COLL, respBean.getRjctcd(), chnCollectSingleLogDO.getTxnseqno(), bean.getRspnInf().getSts());
        // 更新订单状态
        String status = HZRspStatus.SUCCESS.getValue().equals(bean.getRspnInf().getSts()) ? RealCPOrdSts.SCUCESS.getValue() : RealCPOrdSts.FAILED.getValue();
        int effRow = orderCollectSingleDAO.updateOrderStatus(chnCollectSingleLogDO.getTxnseqno(), status);
        if (effRow == 0) {
            logger.error("【 no collection single record to update!!!】");
            return new ResultBean(ErrorCodeBusHZ.NONE_PAY_ORDER.getValue(), ErrorCodeBusHZ.NONE_PAY_ORDER.getDisplayName());
        }
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    private void mainInfoMatchingCheck(BusiTextBean busiTextBean, PayerAndPayeeBean record, BusinessType businessType) throws HZRealBusException {
        boolean isPassCheck = true;
        if (!busiTextBean.getAmt().substring(3, busiTextBean.getAmt().length()).equals(Long.toString(record.getAmount()))) {
            isPassCheck = false;
            logger.error("【" + busiTextBean.getAmt() + "】-【" + Long.toString(record.getAmount()) + "】");
        }
        else if (!busiTextBean.getCdtrAcct().equals(record.getCreditoraccountno())) {
            isPassCheck = false;
            logger.error("【" + busiTextBean.getCdtrAcct() + "】-【" + record.getCreditoraccountno() + "】");
        }
        else if (!busiTextBean.getCdtrBk().equals(record.getCreditorbranchcode())) {
            isPassCheck = false;
            logger.error("【" + busiTextBean.getCdtrBk() + "】-【" + record.getCreditorbranchcode() + "】");
        }
        else if (!BusinessType.REAL_TIME_COLL.equals(businessType) && !busiTextBean.getCdtrNm().equals(record.getCreditorname())) {
            isPassCheck = false;
            logger.error("【" + busiTextBean.getCdtrNm() + "】-【" + record.getCreditorname() + "】");
        }
        else if (!busiTextBean.getDbtrAcct().equals(record.getDebtoraccountno())) {
            isPassCheck = false;
            logger.error("【" + busiTextBean.getDbtrAcct() + "】-【" + record.getDebtoraccountno() + "】");
        }
        else if (!busiTextBean.getDbtrBk().equals(record.getDebtorbranchcode())) {
            isPassCheck = false;
            logger.error("【" + busiTextBean.getDbtrBk() + "】-【" + record.getDebtorbranchcode() + "】");
        }
        else if (!BusinessType.REAL_TIME_PAY.equals(businessType) && !busiTextBean.getDbtrNm().equals(record.getDebtorname())) {
            isPassCheck = false;
            logger.error("【" + busiTextBean.getDbtrNm() + "】-【" + record.getDebtorname() + "】");
        }
        if (!isPassCheck) {
            logger.error("【response main information doesn't match orignal trade record!!!】");
            throw new HZRealBusException(ErrorCodeBusHZ.RSP_NOT_MATCH);
        }
    }

    @Override
    public ResultBean realTimePaymentReceipt(MessageRespBean messageRespBean) {
        CMT387Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMT387Bean.class);
        String msgId = bean.getOrgnlMsgId().getOrgnlMsgId();
        // 回执判重
        ChnPaymentSingleLogDO record = tChnPaymentSingleLogDAO.getPaySingleByMsgId(msgId);
        if (record != null) {
            if (bean.getMsgId().equals(record.getRspmsgid()) || !HZRspStatus.UNKNOWN.getValue().equals(record.getRspstatus())) {
                logger.error("【repeat response and msgId is】 : " + record.getRspmsgid());
                return new ResultBean(ErrorCodeBusHZ.REPEAT_RESP.getValue(), ErrorCodeBusHZ.REPEAT_RESP.getDisplayName());
            }
        }
        else {
            logger.error("【no orignal trade record !!!】  ");
        }
        // 关键信息匹配
        try {
            PayerAndPayeeBean payAndPayBean = new PayerAndPayeeBean();
            BeanUtils.copyProperties(record, payAndPayBean);
            mainInfoMatchingCheck(bean.getBusiText(), payAndPayBean, null);
        }
        catch (HZRealBusException e) {
            logger.error(e.getErrCode() + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        // 更新流水记录
        ChnPaymentSingleLogDO chnPaymentSingleLogDO = tChnPaymentSingleLogDAO.updateRealPaymentLog(bean);
        // 更新主流水记录
        RspnInfBean respBean = bean.getRspnInf();
        updateTxnsLogInfo(BusinessType.REAL_TIME_PAY, respBean.getRjctcd(), chnPaymentSingleLogDO.getTxnseqno(), bean.getRspnInf().getSts());
        // 更新订单状态
        String status = HZRspStatus.SUCCESS.getValue().equals(bean.getRspnInf().getSts()) ? RealCPOrdSts.SCUCESS.getValue() : RealCPOrdSts.FAILED.getValue();
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

        ChnCollectSingleLogDO collDo = tChnCollectSingleLogDAO.getCollSingleByTxId(orgnlTxId);
        if (collDo == null) {
            ChnPaymentSingleLogDO payDo = tChnPaymentSingleLogDAO.getCollSingleByTxId(orgnlTxId);
            if (payDo != null) {
                // 回执判重
                if (bean.getMsgId().equals(payDo.getRspmsgid()) || !HZRspStatus.UNKNOWN.getValue().equals(payDo.getRspstatus())) {
                    logger.error("【repeat response and msgId is】 : " + payDo.getRspmsgid());
                    return new ResultBean(ErrorCodeBusHZ.REPEAT_RESP.getValue(), ErrorCodeBusHZ.REPEAT_RESP.getDisplayName());
                }
                // 关键信息匹配
                try {
                    PayerAndPayeeBean payAndPayBean = new PayerAndPayeeBean();
                    BeanUtils.copyProperties(payDo, payAndPayBean);
                    mainInfoMatchingCheck(bean.getBusiText(), payAndPayBean, BusinessType.REAL_TIME_PAY);
                }
                catch (HZRealBusException e) {
                    logger.error(e.getErrCode() + e.getErrMsg());
                    return new ResultBean(e.getErrCode(), e.getErrMsg());
                }
                // 更新流水记录
                ChnPaymentSingleLogDO chnPaymentSingleLogDO = tChnPaymentSingleLogDAO.updateRealPaymentLog(bean, payDo.getTid());
                // 更新主流水记录
                RspnInfBean respBean = bean.getRspnInf();
                updateTxnsLogInfo(BusinessType.REAL_TIME_PAY, respBean.getRjctcd(), chnPaymentSingleLogDO.getTxnseqno(), bean.getRspnInf().getSts());
                // 更新订单状态
                String status = HZRspStatus.SUCCESS.getValue().equals(bean.getRspnInf().getSts()) ? RealCPOrdSts.SCUCESS.getValue() : RealCPOrdSts.FAILED.getValue();
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
            if (bean.getMsgId().equals(collDo.getRspmsgid()) || !HZRspStatus.UNKNOWN.getValue().equals(collDo.getRspstatus())) {
                logger.error("【repeat response and msgId is】 : " + collDo.getRspmsgid());
                return new ResultBean(ErrorCodeBusHZ.REPEAT_RESP.getValue(), ErrorCodeBusHZ.REPEAT_RESP.getDisplayName());
            }
            // 关键信息匹配
            try {
                PayerAndPayeeBean payAndPayBean = new PayerAndPayeeBean();
                BeanUtils.copyProperties(collDo, payAndPayBean);
                mainInfoMatchingCheck(bean.getBusiText(), payAndPayBean, BusinessType.REAL_TIME_COLL);
            }
            catch (HZRealBusException e) {
                logger.error(e.getErrCode() + e.getErrMsg());
                return new ResultBean(e.getErrCode(), e.getErrMsg());
            }
            // 更新流水记录
            ChnCollectSingleLogDO chnCollectSingleLogDAO = tChnCollectSingleLogDAO.updateRealCollectLog(bean, collDo.getTid());
            // 更新主流水记录
            RspnInfBean respBean = bean.getRspnInf();
            updateTxnsLogInfo(BusinessType.REAL_TIME_PAY, respBean.getRjctcd(), chnCollectSingleLogDAO.getTxnseqno(), bean.getRspnInf().getSts());
            // 更新订单状态
            String status = HZRspStatus.SUCCESS.getValue().equals(bean.getRspnInf().getSts()) ? RealCPOrdSts.SCUCESS.getValue() : RealCPOrdSts.FAILED.getValue();
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
     * @param status
     */
    private void updateTxnsLogInfo(BusinessType businessType, String retCode, String txnSeq, String rspStatus) {
        TransLogUpBean transLogUpBean = new TransLogUpBean();
        transLogUpBean.setBusinessType(businessType);
        transLogUpBean.setTxnseqno(txnSeq);
        transLogUpBean.setPayretcode(retCode);
        String status = HZRspStatus.SUCCESS.getValue().equals(rspStatus) ? TradeStatFlagEnum.FINISH_SUCCESS.getStatus() : TradeStatFlagEnum.FINISH_FAILED.getStatus();
        transLogUpBean.setRspStatus(status);
        txnsLogDAO.updatePayInfoResult(transLogUpBean);
    }

}
