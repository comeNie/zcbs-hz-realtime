package com.zcbspay.platform.hz.realtime.business.message.dao;

import com.zcbspay.platform.hz.realtime.business.message.pojo.OrderPaymentSingleDO;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;

public interface OrderPaymentSingleDAO extends BaseDAO<OrderPaymentSingleDO> {

    /**
     * 更新订单状态
     * 
     * @param txnseqno
     *            交易序列号
     */
    public int updateOrderStatus(String txnseqno, String status);

    /**
     * 通过订单号获取代付订单信息
     * 
     * @param tn
     * @return
     */
    public OrderPaymentSingleDO getPaymentSingleOrderByTN(String tn);

}
