package org.hz.realtime.business.message.impl;

import org.hz.realtime.business.message.dao.OrderCollectSingleDAO;
import org.hz.realtime.business.message.dao.OrderPaymentSingleDAO;
import org.hz.realtime.business.message.dao.TChnCollectSingleLogDAO;
import org.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import org.hz.realtime.business.message.enums.BusStat;
import org.hz.realtime.business.message.enums.ReturnInfo;
import org.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;
import org.hz.realtime.business.message.pojo.TChnPaymentSingleLogDO;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.business.message.service.BusinessMessageReceiver;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.MessageRespBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS317Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT385Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT387Bean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;

public class BusinessMessageReceiverImpl implements BusinessMessageReceiver {

    @Autowired
    private TChnCollectSingleLogDAO tChnCollectSingleLogDAO;
    @Autowired
    private TChnPaymentSingleLogDAO tChnPaymentSingleLogDAO;
    @Autowired
    private OrderCollectSingleDAO orderCollectSingleDAO;
    @Autowired
    private OrderPaymentSingleDAO orderPaymentSingleDAO;

    @Override
    public ResultBean realTimeCollectionChargesReceipt(MessageRespBean messageRespBean) {
        // 更新流水记录
        CMT385Bean bean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMT385Bean.class);
        TChnCollectSingleLogDO chnCollectSingleLogDO = tChnCollectSingleLogDAO.updateRealCollectLog(bean);
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
        ResultBean resultBean = null;
        CMS317Bean realTimePayRespBean = JSONObject.parseObject(messageRespBean.getMsgBody(), CMS317Bean.class);
        String orgMsgType = realTimePayRespBean.getOrgnlTx().getOrgnlMsgType();
        if (MessageTypeEnum.CMT384.value().equals(orgMsgType)) {
            resultBean = realTimeCollectionChargesReceipt(messageRespBean);
        }
        else if (MessageTypeEnum.CMT386.value().equals(orgMsgType)) {
            resultBean = realTimePaymentReceipt(messageRespBean);
        }
        return resultBean;
    }

    @Override
    public ResultBean busStaQryResp(MessageRespBean messageRespBean) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultBean commProcAfrmResp(MessageRespBean messageRespBean) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultBean detectResponse(MessageRespBean messageRespBean) {
        // TODO Auto-generated method stub
        return null;
    }

}
