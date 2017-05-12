package com.zcbspay.platform.hz.realtime.application.dao;

import com.zcbspay.platform.hz.realtime.application.pojo.OrderPaymentSingleDO;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;

public interface OrderPaymentSingleDAO extends BaseDAO<OrderPaymentSingleDO> {

    /**
     * 更新订单状态为失败
     * 
     * @param txnseqno
     *            交易序列号
     */
    public void updateOrderToFail(String txnseqno);

    /**
     * 更新订单状态为成功
     * 
     * @param txnseqno
     *            交易序列号
     */
    public void updateOrderToSuccess(String txnseqno);

    /**
     * 更新订单状态为成功
     * 
     * @param tn
     *            受理订单号
     */
    public void updateOrderToSuccessByTN(String tn);

    /**
     * 通过订单号获取代付订单信息
     * 
     * @param tn
     * @return
     */
    public OrderPaymentSingleDO getPaymentSingleOrderByTN(String tn);

    /**
     * 通过订单号获取代付订单信息
     * 
     * @param tn
     * @return
     */
    public OrderPaymentSingleDO getPaySingOrdByTxnseqno(String txnseqno);
}
