package com.zcbspay.platform.hz.realtime.business.message.enums;

/**
 * 交易所涉流水表标志位
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2017年3月29日 下午4:15:18
 * @since
 */
public enum TradeTxnFlagEnum {

	/**
	 * 1：有     0： 无
	 * 标志位定义
	 * 0位：t_txns_log
	 * 1位：t_order_collect_batch
	 * 2位：t_order_collect_deta
	 * 3位：t_order_collect_single
	 * 4位：t_order_payment_batch
	 * 5位：t_order_payment_deta
	 * 6位：t_order_payment_single
	 * 7位：t_chn_collect_batch
	 * 8位：t_chn_collect_deta
	 * 9位：t_chn_collect_single_log
	 * 10位：t_chn_payment_batch
	 * 11位：t_chn_payment_deta
	 * 12位：t_chn_payment_single_log
	 */
	
	HZ_BATCH_COLLECT("1110000110000"),
	HZ_BATCH_PAYMENT("1000110000110"),
	HZ_REALTIME_COLL("1001000001000"),
	HZ_REALTIME_PAY("1000001000001"),
	;
	String code;

	private TradeTxnFlagEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
}
