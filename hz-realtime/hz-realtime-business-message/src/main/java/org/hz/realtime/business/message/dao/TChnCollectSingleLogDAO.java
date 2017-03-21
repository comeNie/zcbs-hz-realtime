package org.hz.realtime.business.message.dao;

import org.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;

public interface TChnCollectSingleLogDAO extends BaseDAO<TChnCollectSingleLogDO> {
    
    /**
     * 保存实时代付流水信息
     * @param collectionChargesBean
     * @return
     */
    public TChnCollectSingleLogDO saveRealCollectLog(SingleCollectionChargesBean collectionChargesBean);

}
