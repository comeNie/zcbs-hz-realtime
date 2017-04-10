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
    public OrderCollectSingleDO getCollectSingleOrderByTN(String tn);

    /**
     * 更新订单状态
     * 
     * @param tn订单号
     * 
     */
    public int updateOrderStatus(String tn, String status);
}
