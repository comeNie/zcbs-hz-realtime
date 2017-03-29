package com.zcbspay.platform.hz.realtime.business.message.dao.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.hz.realtime.business.message.dao.TChnCollectSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateStyle;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateTimeUtils;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS911Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT385Bean;

@Repository
public class TChnCollectSingleLogDAOImpl extends HibernateBaseDAOImpl<TChnCollectSingleLogDO> implements TChnCollectSingleLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(TChnCollectSingleLogDAOImpl.class);
    @Resource(name = "redisSerialNumberService")
    private SerialNumberService redisSerialNumberService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public TChnCollectSingleLogDO saveRealCollectLog(SingleCollectionChargesBean collectionChargesBean, String msgId, String comRefId) {
        // 记录实时代收流水(T_CHN_COLLECT_SINGLE_LOG)
        TChnCollectSingleLogDO chnCollectSingleLog = new TChnCollectSingleLogDO();
        chnCollectSingleLog.setTid(redisSerialNumberService.generateDBPrimaryKey());
        chnCollectSingleLog.setMsgid(msgId);
        chnCollectSingleLog.setTxid(collectionChargesBean.getTxId());
        chnCollectSingleLog.setDebtorname(collectionChargesBean.getDebtorName());
        chnCollectSingleLog.setDebtoraccountno(collectionChargesBean.getDebtorAccountNo());
        chnCollectSingleLog.setCreditorbranchcode(collectionChargesBean.getCreditorBranchCode());
        chnCollectSingleLog.setCreditorname(collectionChargesBean.getCreditorName());
        chnCollectSingleLog.setCreditoraccountno(collectionChargesBean.getCreditorAccountNo());
        chnCollectSingleLog.setAmount(Long.parseLong(collectionChargesBean.getAmount()));
        chnCollectSingleLog.setPurposeproprietary(collectionChargesBean.getPurposeCode());
        chnCollectSingleLog.setEndtoendidentification(collectionChargesBean.getEndToEndIdentification());
        chnCollectSingleLog.setSummary(collectionChargesBean.getSummary());
        chnCollectSingleLog.setTxnseqno(collectionChargesBean.getTxnseqno());
        // 借用Notes备注字段储存通讯级参考号,用于丢弃报文匹配原交易
        chnCollectSingleLog.setNotes(comRefId);
        saveEntity(chnCollectSingleLog);
        return chnCollectSingleLog;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public TChnCollectSingleLogDO updateRealCollectLog(CMT385Bean realTimeCollRespBean) {
        String hql = "from TChnCollectSingleLogDO where msgId=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, realTimeCollRespBean.getOrgnlMsgId().getOrgnlMsgId());
        TChnCollectSingleLogDO collSingle = (TChnCollectSingleLogDO) query.uniqueResult();
        collSingle.setRspmsgid(realTimeCollRespBean.getMsgId());
        collSingle.setRspstatus(realTimeCollRespBean.getRspnInf().getSts());
        collSingle.setRsprejectcode(realTimeCollRespBean.getRspnInf().getRjctcd());
        collSingle.setRsprejectinformation(realTimeCollRespBean.getRspnInf().getRjctinf());
        collSingle.setRspdate(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        collSingle.setNettingdate(realTimeCollRespBean.getRspnInf().getNetgdt());
        TChnCollectSingleLogDO retDo = update(collSingle);
        return retDo;
    }

    @Override
    public TChnCollectSingleLogDO getCollSingleByTxnseqno(String txnseqno) {
        String hql = "from TChnCollectSingleLogDO where txnseqno=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, txnseqno);
        return (TChnCollectSingleLogDO) query.uniqueResult();
    }

    @Override
    public void updateRealCollectLogCommResp(CMS900Bean bean) {
        String hql = "update TChnCollectSingleLogDO set commsgid = ? , comstatus = ? ,comrejectcode=? ,comrejectinformation=? where msgid=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, bean.getMsgId());
        query.setString(1, bean.getRspnInf().getSts());
        query.setString(2, bean.getRspnInf().getRjctcd());
        query.setString(3, bean.getRspnInf().getRjctinf());
        query.setString(4, bean.getOrgnlMsgId().getOrgnlMsgId());
        int rows = query.executeUpdate();
        logger.info("updateRealCollectLogCommResp() effect rows:" + rows);
    }

    @Override
    public void updateRealCollectLogDiscard(CMS911Bean bean) {
        String hql = "update TChnCollectSingleLogDO set commsgid = ? ,comrejectcode=? ,comrejectinformation=? where notes=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, bean.getMsgId());
        query.setString(1, bean.getDscrdInf().getRjctCd());
        query.setString(2, bean.getDscrdInf().getRjctInf());
        query.setString(3, bean.getDscrdInf().getRefId());
        int rows = query.executeUpdate();
        logger.info("updateRealCollectLogDiscard() effect rows:" + rows);

    }

}
