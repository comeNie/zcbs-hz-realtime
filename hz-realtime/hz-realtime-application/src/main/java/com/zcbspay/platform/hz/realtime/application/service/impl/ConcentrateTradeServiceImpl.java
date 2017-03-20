package com.zcbspay.platform.hz.realtime.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zcbspay.platform.hz.realtime.application.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.hz.realtime.application.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.hz.realtime.application.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.hz.realtime.application.pojo.OrderPaymentSingleDO;
import com.zcbspay.platform.hz.realtime.application.service.ConcentrateTradeService;
import com.zcbspay.platform.hz.realtime.application.service.bean.TradeBean;
import com.zcbspay.platform.hz.realtime.business.message.service.BusinesssMessageSender;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.utils.BeanCopyUtil;


@Service
public class ConcentrateTradeServiceImpl implements ConcentrateTradeService {

	@Autowired
	private OrderCollectSingleDAO orderCollectSingleDAO;
	@Autowired
	private OrderPaymentSingleDAO orderPaymentSingleDAO;
	@Reference(version="1.0")
	private BusinesssMessageSender businesssMessageSender;
	
	
	@Override
	public ResultBean realTimeCollection(TradeBean tradeBean) {
		OrderCollectSingleDO orderInfo = orderCollectSingleDAO.getCollectSingleOrderByTN(tradeBean.getTn());
		SingleCollectionChargesBean collectionChargesBean = new SingleCollectionChargesBean();
		collectionChargesBean.setCreditorAccountNo(orderInfo.getCreditoraccount());
		collectionChargesBean.setCreditorBranchCode(orderInfo.getCreditorbank());
		collectionChargesBean.setCreditorName(orderInfo.getCreditorname());
		collectionChargesBean.setDebtorAccountNo(orderInfo.getDebtoraccount());
		collectionChargesBean.setDebtorBranchCode(orderInfo.getDebtorbank());
		collectionChargesBean.setDebtorName(orderInfo.getDebtorname());
		collectionChargesBean.setAmount(orderInfo.getTxnamt().toString());
		collectionChargesBean.setPurposeCode(orderInfo.getProprietary());
		collectionChargesBean.setEndToEndIdentification(orderInfo.getDebtorconsign());
		collectionChargesBean.setSummary(orderInfo.getSummary());
		collectionChargesBean.setTxnseqno(orderInfo.getRelatetradetxn());
		com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean resultBean = businesssMessageSender.realTimeCollectionCharges(collectionChargesBean );
		if(resultBean.isResultBool()){
			orderPaymentSingleDAO.updateOrderToSuccess(orderInfo.getRelatetradetxn());
		}else{
			orderPaymentSingleDAO.updateOrderToFail(orderInfo.getRelatetradetxn());
		}
		return BeanCopyUtil.copyBean(ResultBean.class, resultBean);
	}

	@Override
	public ResultBean realTimePayment(TradeBean tradeBean) {
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
		com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean resultBean = businesssMessageSender.realTimePayment(paymentBean);
		if(resultBean.isResultBool()){
			orderCollectSingleDAO.updateOrderToSuccess(orderInfo.getRelatetradetxn());
		}else{
			orderCollectSingleDAO.updateOrderToFail(orderInfo.getRelatetradetxn());
		}
		return BeanCopyUtil.copyBean(ResultBean.class, resultBean);
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
