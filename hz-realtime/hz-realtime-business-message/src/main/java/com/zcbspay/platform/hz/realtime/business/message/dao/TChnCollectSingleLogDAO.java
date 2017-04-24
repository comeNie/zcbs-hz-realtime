package com.zcbspay.platform.hz.realtime.business.message.dao;

import com.zcbspay.platform.hz.realtime.business.message.pojo.ChnCollectSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;
import com.zcbspay.platform.hz.realtime.message.bean.CMS317Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS911Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT385Bean;

public interface TChnCollectSingleLogDAO extends BaseDAO<ChnCollectSingleLogDO> {

    /**
     * 保存实时代收流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public ChnCollectSingleLogDO saveRealCollectLog(SingleCollectionChargesBean collectionChargesBean, String msgId, String comRefId);

    /**
     * 更新实时代收流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public ChnCollectSingleLogDO updateRealCollectLog(CMT385Bean bean);

    /**
     * 更新实时代收流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public ChnCollectSingleLogDO updateRealCollectLog(CMS317Bean queryBusStsRsp, long tid);

    /**
     * 通过txnseqno获取非失败状态的代收记录
     * 
     * @param collectionChargesBean
     * @return
     */
    public ChnCollectSingleLogDO getCollSingleByTxnseqnoNotFail(String txnseqno);

    /**
     * 通过业务流水号和业务应答状态获取报文记录
     * 
     * @param txnseqno
     * @param rspStatus
     * @return
     */
    public ChnCollectSingleLogDO getCollSingleByTxnseqnoAndRspSta(String txnseqno, String... rspStatus);

    /**
     * 通过msgId获取代收记录
     * 
     * @param msgId
     * @return
     */
    public ChnCollectSingleLogDO getCollSingleByMsgId(String msgId);

    /**
     * 通过明细标识号获取代收记录
     * 
     * @param collectionChargesBean
     * @return
     */
    public ChnCollectSingleLogDO getCollSingleByTxId(String txId);

    /**
     * 通过tid获取代收记录
     * 
     * @param collectionChargesBean
     * @return
     */
    public ChnCollectSingleLogDO getCollSingleByTid(long tid);

    /**
     * 更新实时代收流水通用回执信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public void updateRealCollectLogCommResp(CMS900Bean bean);

    /**
     * 更新实时代收流水丢弃报文信息
     * 
     * @param bean
     */
    public void updateRealCollectLogDiscard(CMS911Bean bean);

    /**
     * 更新实时代收渠道流水发送信息
     * 
     * @param bean
     */
    public void updateRealCollLogSendInfo(long tid, String status, String errCodeMsg);

}
