package com.zcbspay.platform.hz.realtime.application.service;



import com.zcbspay.platform.hz.realtime.application.service.bean.TradeBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;

/**
 * 集中代收付交易接口
 *
 * @author guojia
 * @version
 * @date 2017年3月17日 下午2:33:45
 * @since
 */
public interface ConcentrateTradeService {

	/**
	 * 实时代收
	 * @param tradeBean
	 * @return
	 */
	public ResultBean realTimeCollection(TradeBean tradeBean);
	
	/**
	 * 实时代付
	 * @param tradeBean
	 * @return
	 */
	public ResultBean realTimePayment(TradeBean tradeBean);
	
	/**
	 * 交易查询
	 * @param txnseqno
	 * @return
	 */
	public ResultBean queryTrade(String txnseqno);
	
	public ResultBean checkLink();
}
