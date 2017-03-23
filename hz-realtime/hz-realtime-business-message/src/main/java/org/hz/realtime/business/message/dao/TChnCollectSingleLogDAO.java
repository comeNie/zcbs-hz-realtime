package org.hz.realtime.business.message.dao;

import org.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS911Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT385Bean;

public interface TChnCollectSingleLogDAO extends BaseDAO<TChnCollectSingleLogDO> {

    /**
     * 保存实时代收流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public TChnCollectSingleLogDO saveRealCollectLog(SingleCollectionChargesBean collectionChargesBean, String msgId, String comRefId);

    /**
     * 更新实时代收流水信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public TChnCollectSingleLogDO updateRealCollectLog(CMT385Bean bean);

    /**
     * 通过txnseqno获取代收记录
     * 
     * @param collectionChargesBean
     * @return
     */
    public TChnCollectSingleLogDO getCollSingleByTxnseqno(String txnseqno);

    /**
     * 更新实时代收流水通用回执信息
     * 
     * @param collectionChargesBean
     * @return
     */
    public void updateRealCollectLogCommResp(CMS900Bean bean);

    public void updateRealCollectLogDiscard(CMS911Bean bean);

}
