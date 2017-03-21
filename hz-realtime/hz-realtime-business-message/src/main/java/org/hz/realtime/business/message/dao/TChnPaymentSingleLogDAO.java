package org.hz.realtime.business.message.dao;

import org.hz.realtime.business.message.bean.RealTimeCollRespBean;
import org.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;
import org.hz.realtime.business.message.pojo.TChnPaymentSingleLogDO;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;

public interface TChnPaymentSingleLogDAO extends BaseDAO<TChnPaymentSingleLogDO> {

    /**
     * 保存实时代付流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public TChnPaymentSingleLogDO saveRealPaymentLog(SinglePaymentBean paymentBean);

    /**
     * 更新实时代付流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public TChnCollectSingleLogDO updateRealPaymentLog(RealTimeCollRespBean realTimeCollRespBean);

}
