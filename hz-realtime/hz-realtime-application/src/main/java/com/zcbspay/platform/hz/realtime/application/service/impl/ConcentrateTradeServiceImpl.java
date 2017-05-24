package com.zcbspay.platform.hz.realtime.application.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zcbspay.platform.hz.realtime.application.dao.HzAgencyInfoDAO;
import com.zcbspay.platform.hz.realtime.application.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.hz.realtime.application.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.hz.realtime.application.dao.TxnsLogDAO;
import com.zcbspay.platform.hz.realtime.application.enums.BusinessType;
import com.zcbspay.platform.hz.realtime.application.enums.RealCPOrdSts;
import com.zcbspay.platform.hz.realtime.application.enums.TradeStatFlagEnum;
import com.zcbspay.platform.hz.realtime.application.pojo.HzAgencyInfoDO;
import com.zcbspay.platform.hz.realtime.application.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.hz.realtime.application.pojo.OrderPaymentSingleDO;
import com.zcbspay.platform.hz.realtime.application.pojo.TxnsLogDO;
import com.zcbspay.platform.hz.realtime.application.service.ConcentrateTradeService;
import com.zcbspay.platform.hz.realtime.application.service.bean.TradeBean;
import com.zcbspay.platform.hz.realtime.business.message.service.BusinesssMessageSender;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.business.message.service.enums.ErrorCodeBusHZ;
import com.zcbspay.platform.hz.realtime.business.message.service.exception.HZRealBusException;
import com.zcbspay.platform.hz.realtime.common.utils.BeanCopyUtil;

@Service
public class ConcentrateTradeServiceImpl implements ConcentrateTradeService {

    private static final Logger logger = LoggerFactory.getLogger(ConcentrateTradeServiceImpl.class);

    @Autowired
    private OrderCollectSingleDAO orderCollectSingleDAO;
    @Autowired
    private OrderPaymentSingleDAO orderPaymentSingleDAO;
    @Reference(version = "1.0")
    private BusinesssMessageSender businesssMessageSender;
    @Autowired
    private HzAgencyInfoDAO hzAgencyInfoDAO;
    @Autowired
    private TxnsLogDAO txnsLogDAO;

    @Override
    public ResultBean realTimeCollection(TradeBean tradeBean) {
        com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean resultBean = null;
        try {
            businessCheckColl(tradeBean);
            OrderCollectSingleDO orderInfo = orderCollectSingleDAO.getCollectSingleOrderByTN(tradeBean.getTn());
            SingleCollectionChargesBean collectionChargesBean = new SingleCollectionChargesBean();
            collectionChargesBean.setCreditorAccountNo(orderInfo.getCreditoraccount());
            collectionChargesBean.setCreditorBranchCode(orderInfo.getCreditorbank());
            collectionChargesBean.setCreditorName(orderInfo.getCreditorname());
            collectionChargesBean.setDebtorAccountNo(orderInfo.getDebtoraccount());
            collectionChargesBean.setDebtorBranchCode(orderInfo.getDebtorbank());
            collectionChargesBean.setDebtorName(orderInfo.getDebtorname());
            collectionChargesBean.setAmount(orderInfo.getTxnamt().toString());
            collectionChargesBean.setEndToEndIdentification(orderInfo.getDebtorconsign());
            collectionChargesBean.setSummary(orderInfo.getSummary());
            collectionChargesBean.setTxnseqno(orderInfo.getRelatetradetxn());
            collectionChargesBean.setTxId(txnsLogDAO.getTxnsLogByTxnseqno(tradeBean.getTxnseqno()).getPayordno());
            HzAgencyInfoDO hzAgencyInfoDO = hzAgencyInfoDAO.getHzAgencyInfoByMerIdBusTyp(orderInfo.getMerid(), BusinessType.REAL_TIME_COLL.getValue());
            collectionChargesBean.setSenderOrgCode(hzAgencyInfoDO.getChargingunit());
            collectionChargesBean.setPurposeCode(hzAgencyInfoDO.getBusisort());
            resultBean = businesssMessageSender.realTimeCollectionCharges(collectionChargesBean);
        }
        catch (HZRealBusException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        return BeanCopyUtil.copyBean(ResultBean.class, resultBean);
    }

    private void businessCheckColl(TradeBean tradeBean) throws HZRealBusException {
        String txnseqno = tradeBean.getTxnseqno();
        // 主流水校验
        checkTnxLog(txnseqno);
        // 代收订单状态校验
        OrderCollectSingleDO order = orderCollectSingleDAO.getCollSingOrdByTxnseqno(txnseqno);
        if (order == null || !order.getTn().equals(tradeBean.getTn())) {
            logger.error("【 no collection single order record to update!!!】" + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.NONE_PAY_ORDER.getValue(), ErrorCodeBusHZ.NONE_PAY_ORDER.getDisplayName());
        }
        if (!RealCPOrdSts.PAYING.getValue().equals(order.getStatus()) && !RealCPOrdSts.FAILED.getValue().equals(order.getStatus())) {
            logger.error("【OrderCollectSingleDO status is wrong and it's rejected!!!】" + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.ORDER_STS_WRONG.getValue(), ErrorCodeBusHZ.ORDER_STS_WRONG.getDisplayName());
        }
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
    public ResultBean realTimePayment(TradeBean tradeBean) {
        com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean resultBean = null;
        try {
            businessCheckPay(tradeBean);
            OrderPaymentSingleDO orderInfo = orderPaymentSingleDAO.getPaymentSingleOrderByTN(tradeBean.getTn());
            SinglePaymentBean paymentBean = new SinglePaymentBean();
            paymentBean.setCreditorAccountNo(orderInfo.getCreditoraccount());
            paymentBean.setCreditorBranchCode(orderInfo.getCreditorbank());
            paymentBean.setCreditorName(orderInfo.getCreditorname());
            paymentBean.setDebtorAccountNo(orderInfo.getDebtoraccount());
            paymentBean.setDebtorBranchCode(orderInfo.getDebtorbank());
            paymentBean.setDebtorName(orderInfo.getDebtorname());
            paymentBean.setAmount(orderInfo.getTxnamt().toString());
            paymentBean.setPurposeCode(orderInfo.getProprietary());
            paymentBean.setEndToEndIdentification(orderInfo.getDebtorconsign());
            paymentBean.setSummary(orderInfo.getSummary());
            paymentBean.setTxnseqno(orderInfo.getRelatetradetxn());
            paymentBean.setTxId(txnsLogDAO.getTxnsLogByTxnseqno(tradeBean.getTxnseqno()).getPayordno());
            HzAgencyInfoDO hzAgencyInfoDO = hzAgencyInfoDAO.getHzAgencyInfoByMerIdBusTyp(orderInfo.getMerid(), BusinessType.REAL_TIME_PAY.getValue());
            paymentBean.setSenderOrgCode(hzAgencyInfoDO.getChargingunit());
            paymentBean.setPurposeCode(hzAgencyInfoDO.getBusisort());
            resultBean = businesssMessageSender.realTimePayment(paymentBean);
        }
        catch (HZRealBusException e) {
            logger.error(e.getErrCode() + "" + e.getErrMsg());
            return new ResultBean(e.getErrCode(), e.getErrMsg());
        }
        return BeanCopyUtil.copyBean(ResultBean.class, resultBean);
    }

    private String businessCheckPay(TradeBean tradeBean) throws HZRealBusException {
        String txnseqno = tradeBean.getTxnseqno();
        // 主流水校验
        checkTnxLog(txnseqno);
        // 代付订单状态校验
        OrderPaymentSingleDO order = orderPaymentSingleDAO.getPaySingOrdByTxnseqno(txnseqno);
        if (order == null || !order.getTn().equals(tradeBean.getTn())) {
            logger.error("【 no collection single order record to update!!!】" + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.NONE_PAY_ORDER.getValue(), ErrorCodeBusHZ.NONE_PAY_ORDER.getDisplayName());
        }
        if (!RealCPOrdSts.PAYING.getValue().equals(order.getStatus()) && !RealCPOrdSts.FAILED.getValue().equals(order.getStatus())) {
            logger.error("【OrderCollectSingleDO status is wrong and it's rejected!!!】" + txnseqno);
            throw new HZRealBusException(ErrorCodeBusHZ.ORDER_STS_WRONG.getValue(), ErrorCodeBusHZ.ORDER_STS_WRONG.getDisplayName());
        }
        return order.getMerid();
    }

    @Override
    public ResultBean queryTrade(String txnseqno) {
        com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean resultBean = businesssMessageSender.queryTrade(txnseqno);
        return BeanCopyUtil.copyBean(ResultBean.class, resultBean);
    }

    @Override
    public ResultBean checkLink() {
        com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean resultBean = businesssMessageSender.check();
        return BeanCopyUtil.copyBean(ResultBean.class, resultBean);
    }

}
