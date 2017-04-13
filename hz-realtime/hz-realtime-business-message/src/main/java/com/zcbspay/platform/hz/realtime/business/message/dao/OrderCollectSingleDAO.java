package com.zcbspay.platform.hz.realtime.business.message.dao;

import com.zcbspay.platform.hz.realtime.business.message.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;

public interface OrderCollectSingleDAO extends BaseDAO<OrderCollectSingleDO> {

    /**
     * 通过tn获取实时代收订单
     * 
     * @param tn
     * @return
     */
    public OrderCollectSingleDO getCollSingOrdByTxnseqno(String txnseqno);

    /**
     * 更新订单状态
     * 
     * @param relatetradetxn
     * 
     */
    public int updateOrderStatus(String relatetradetxn, String status);
}
