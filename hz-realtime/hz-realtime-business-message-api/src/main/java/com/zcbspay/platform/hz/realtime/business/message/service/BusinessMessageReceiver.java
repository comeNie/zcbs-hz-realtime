package com.zcbspay.platform.hz.realtime.business.message.service;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;

/**
 * 业务报文接收器
 *
 * @author guojia
 * @version
 * @date 2017年3月9日 下午4:07:57
 * @since
 */
public interface BusinessMessageReceiver {

	/**
	 * 实时代收回执报文处理方法
	 * @param msg
	 * @return
	 */
	public ResultBean realTimeCollectionChargesReceipt(String msg);
	
	/**
	 * 实时代付回执报文处理方法
	 * @param msg
	 * @return
	 */
	public ResultBean realTimePaymentReceipt(String msg);
	
	/**
	 * 丢弃报文处理方法
	 * @param msg
	 * @return
	 */
	public ResultBean discardMessage(String msg);
}
