package com.zcbspay.platform.hz.realtime.business.message.dao;

import com.zcbspay.platform.hz.realtime.business.message.pojo.ChnPaymentSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;
import com.zcbspay.platform.hz.realtime.message.bean.CMS317Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS911Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT387Bean;

public interface TChnPaymentSingleLogDAO extends BaseDAO<ChnPaymentSingleLogDO> {

    /**
     * 保存实时代付流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public ChnPaymentSingleLogDO saveRealPaymentLog(SinglePaymentBean paymentBean, String msgId, String comRefId, String senderOrgCode);

    /**
     * 更新实时代付流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public ChnPaymentSingleLogDO updateRealPaymentLog(CMT387Bean realTimePayRespBean);

    /**
     * 更新实时代付流水信息
     * 
     * @param tid
     * 
     * @param collectionChargesBean
     * @return
     */
    public ChnPaymentSingleLogDO updateRealPaymentLog(CMS317Bean queryBusStsRsp, long tid);

    /**
     * 通过业务流水号获取非失败状态的报文记录
     * 
     * @param txnseqno
     * @return
     */
    public ChnPaymentSingleLogDO getPaySingleByTxnseqnoNotFail(String txnseqno);

    /**
     * 通过业务流水号和业务应答状态获取报文记录
     * 
     * @param txnseqno
     * @param rspStatus
     * @return
     */
    public ChnPaymentSingleLogDO getPaySingleByTxnseqnoAndRspSta(String txnseqno, String... rspStatus);

    /**
     * 通过msgId获取报文记录
     * 
     * @param msgId
     * @return
     */
    public ChnPaymentSingleLogDO getPaySingleByMsgId(String msgId);

    /**
     * 通过明细标识号获取代收记录
     * 
     * @param collectionChargesBean
     * @return
     */
    public ChnPaymentSingleLogDO getCollSingleByTxId(String txId);

    /**
     * 更新通用回执信息
     * 
     * @param bean
     */
    public void updateRealPaymentLogCommResp(CMS900Bean bean);

    public void updateRealPaymentLogDiscard(CMS911Bean bean);

    public ChnPaymentSingleLogDO getPaySingleByTid(long tid);

    public void updateRealPayLogSendInfo(long tid, String status, String errCodeMsg);

}
