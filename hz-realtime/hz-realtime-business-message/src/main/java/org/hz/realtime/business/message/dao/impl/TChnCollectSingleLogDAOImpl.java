package org.hz.realtime.business.message.dao.impl;

import javax.annotation.Resource;

import org.hz.realtime.business.message.dao.TChnCollectSingleLogDAO;
import org.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;

public class TChnCollectSingleLogDAOImpl extends HibernateBaseDAOImpl<TChnCollectSingleLogDO> implements TChnCollectSingleLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(TChnCollectSingleLogDAOImpl.class);
    @Resource
    private SerialNumberService redisSerialNumberService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public TChnCollectSingleLogDO saveRealCollectLog(SingleCollectionChargesBean collectionChargesBean) {
        // 记录实时代收流水(T_CHN_COLLECT_SINGLE_LOG)
        TChnCollectSingleLogDO chnCollectSingleLog = new TChnCollectSingleLogDO();
        chnCollectSingleLog.setTid(redisSerialNumberService.generateDBPrimaryKey());
        chnCollectSingleLog.setMsgid(redisSerialNumberService.generateHZMsgId());
        chnCollectSingleLog.setTxid(redisSerialNumberService.generateTranIden());
        chnCollectSingleLog.setDebtorname(collectionChargesBean.getDebtorName());
        chnCollectSingleLog.setDebtoraccountno(collectionChargesBean.getDebtorAccountNo());
        chnCollectSingleLog.setCreditorbranchcode(collectionChargesBean.getCreditorBranchCode());
        chnCollectSingleLog.setCreditorname(collectionChargesBean.getCreditorName());
        chnCollectSingleLog.setCreditoraccountno(collectionChargesBean.getCreditorAccountNo());
        chnCollectSingleLog.setAmount(Long.parseLong(collectionChargesBean.getAmount()));
        chnCollectSingleLog.setPurposeproprietary(collectionChargesBean.getPurposeCode());
        chnCollectSingleLog.setEndtoendidentification(collectionChargesBean.getEndToEndIdentification());
        chnCollectSingleLog.setSummary(collectionChargesBean.getSummary());
        saveEntity(chnCollectSingleLog);
        return chnCollectSingleLog;
    }

}
