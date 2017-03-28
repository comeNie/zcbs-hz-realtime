package com.zcbspay.platform.hz.realtime.business.message.dao;

import com.zcbspay.platform.hz.realtime.business.message.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;

public interface OrderCollectSingleDAO extends BaseDAO<OrderCollectSingleDO> {

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
     * 通过tn获取实时代收订单
     * 
     * @param tn
     * @return
     */
    public OrderCollectSingleDO getCollectSingleOrderByTN(String tn);

    /**
     * 更新订单状态为失败
     * 
     * @param tn订单号
     *           
     */
    public void updateOrderToFailByTn(String tn);

    /**
     * 更新订单状态为成功
     * 
     * @param tn订单号
     *           
     */
    public void updateOrderToSuccessByTn(String tn);
}
