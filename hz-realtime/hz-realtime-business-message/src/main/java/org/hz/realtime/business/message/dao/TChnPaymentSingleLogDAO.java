package org.hz.realtime.business.message.dao;

import org.hz.realtime.business.message.pojo.TChnPaymentSingleLogDO;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;
import com.zcbspay.platform.hz.realtime.message.bean.CMT387Bean;

public interface TChnPaymentSingleLogDAO extends BaseDAO<TChnPaymentSingleLogDO> {

    /**
     * 保存实时代付流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public TChnPaymentSingleLogDO saveRealPaymentLog(SinglePaymentBean paymentBean, String msgId);

    /**
     * 更新实时代付流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public TChnPaymentSingleLogDO updateRealPaymentLog(CMT387Bean realTimePayRespBean);

    /**
     * 通过业务流水号获取报文记录
     * @param txnseqno
     * @return
     */
    public TChnPaymentSingleLogDO getPaySingleByTxnseqno(String txnseqno);

}
