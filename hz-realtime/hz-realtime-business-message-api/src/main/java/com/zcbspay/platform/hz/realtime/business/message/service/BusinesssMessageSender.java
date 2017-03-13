package com.zcbspay.platform.hz.realtime.business.message.service;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;

/**
 * 
 * 业务报文发送器
 *
 * @author guojia
 * @version
 * @date 2017年3月9日 下午4:01:24
 * @since
 */
public interface BusinesssMessageSender {

	/**
	 * 实时代收
	 * @param collectionChargesBean
	 * @return
	 */
	public ResultBean realTimeCollectionCharges(SingleCollectionChargesBean collectionChargesBean);
	
	/**
	 * 实时代付
	 * @param paymentBean
	 * @return
	 */
	public ResultBean realTimePayment(SinglePaymentBean paymentBean);
	
	/**
	 * 交易查询
	 * @param txnseqno
	 * @return
	 */
	public ResultBean queryTrade(String txnseqno);
	
	/**
	 * 链路探测
	 * @return
	 */
	public ResultBean check();
}
