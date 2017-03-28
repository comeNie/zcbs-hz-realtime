package com.zcbspay.platform.hz.realtime.business.message.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.business.message.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TChnCollectSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TxnsLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.enums.BusStat;
import com.zcbspay.platform.hz.realtime.business.message.enums.ReturnInfo;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TChnPaymentSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TTxnsLogDO;
import com.zcbspay.platform.hz.realtime.business.message.service.BusinessMessageReceiver;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.MessageRespBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS317Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS911Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS992Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT385Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT387Bean;
import com.zcbspay.platform.hz.realtime.message.bean.RspnInfBean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;

@Service
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
        // 更新流水记录
        CMT385Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMT385Bean.class);
        TChnCollectSingleLogDO chnCollectSingleLogDO = tChnCollectSingleLogDAO.updateRealCollectLog(bean);
        // 更新主流水记录
        RspnInfBean respBean = bean.getRspnInf();
        updateTxnsLogInfo(respBean.getSts(), respBean.getRjctcd(), respBean.getRjctinf(), respBean.getNetgdt(), chnCollectSingleLogDO.getTxnseqno());
        // 更新订单状态
        if (BusStat.SUCCESS.getValue().equals(bean.getRspnInf().getSts())) {
            orderCollectSingleDAO.updateOrderToSuccessByTn(chnCollectSingleLogDO.getTxnseqno());
        }
        else {
            orderCollectSingleDAO.updateOrderToFailByTn(chnCollectSingleLogDO.getTxnseqno());
        }
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean realTimePaymentReceipt(MessageRespBean messageRespBean) {
        // 更新流水记录
        CMT387Bean realTimePayRespBean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMT387Bean.class);
        TChnPaymentSingleLogDO chnPaymentSingleLogDO = tChnPaymentSingleLogDAO.updateRealPaymentLog(realTimePayRespBean);
        // 更新主流水记录
        RspnInfBean respBean = realTimePayRespBean.getRspnInf();
        updateTxnsLogInfo(respBean.getSts(), respBean.getRjctcd(), respBean.getRjctinf(), respBean.getNetgdt(), chnPaymentSingleLogDO.getTxnseqno());
        // 更新订单状态
        if (BusStat.SUCCESS.getValue().equals(realTimePayRespBean.getRspnInf().getSts())) {
            orderPaymentSingleDAO.updateOrderToSuccessByTN(chnPaymentSingleLogDO.getTxnseqno());
        }
        else {
            orderPaymentSingleDAO.updateOrderToFailByTn(chnPaymentSingleLogDO.getTxnseqno());
        }
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean discardMessage(MessageRespBean messageRespBean) {
        CMS911Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMS911Bean.class);
        String orgMsgType = bean.getDscrdInf().getMsgType();
        if (MessageTypeEnum.CMT384.value().equals(orgMsgType)) {
            tChnCollectSingleLogDAO.updateRealCollectLogDiscard(bean);
        }
        else if (MessageTypeEnum.CMT386.value().equals(orgMsgType)) {
            tChnPaymentSingleLogDAO.updateRealPaymentLogDiscard(bean);
        }
        else {
            logger.error("【" + orgMsgType + "】 message is error and the CMS911 info is :");
        }
        return new ResultBean(ReturnInfo.SUCCESS.getValue());
    }

    @Override
    public ResultBean busStaQryResp(MessageRespBean messageRespBean) {
        ResultBean resultBean = null;
        CMS317Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMS317Bean.class);
        String orgMsgType = bean.getOrgnlTx().getOrgnlMsgType();
        if (MessageTypeEnum.CMT384.value().equals(orgMsgType)) {
            resultBean = realTimeCollectionChargesReceipt(messageRespBean);
        }
        else if (MessageTypeEnum.CMT386.value().equals(orgMsgType)) {
            resultBean = realTimePaymentReceipt(messageRespBean);
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

    @Override
    public ResultBean detectResponse(MessageRespBean messageRespBean) {
        CMS992Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMS992Bean.class);
        logger.info("CMS992 is :" + bean.toString());
        return null;
    }

    private void updateTxnsLogInfo(String retSts, String retCode, String retMsg, String settDate, String txnSeq) {
        TTxnsLogDO txnsLog = new TTxnsLogDO();
        txnsLog.setRetcode(retSts);
        txnsLog.setRetinfo(retCode + retMsg);
        txnsLog.setAccsettledate(settDate);
        txnsLog.setTxnseqno(txnSeq);
        txnsLogDAO.updateTxnsLogRespInfo(txnsLog);
    }

}
