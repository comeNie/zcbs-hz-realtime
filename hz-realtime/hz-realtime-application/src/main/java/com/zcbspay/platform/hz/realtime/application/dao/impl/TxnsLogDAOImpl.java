package com.zcbspay.platform.hz.realtime.application.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.hz.realtime.application.dao.TxnsLogDAO;
import com.zcbspay.platform.hz.realtime.application.pojo.TxnsLogDO;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.hz.realtime.common.enums.ChannelCode;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateUtil;

@Repository("txnsLogDAO")
public class TxnsLogDAOImpl extends HibernateBaseDAOImpl<TxnsLogDO> implements TxnsLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(TxnsLogDAOImpl.class);

    @Override
    @Transactional(readOnly = true)
    public TxnsLogDO getTxnsLogByTxnseqno(String txnseqno) {
        Criteria criteria = getSession().createCriteria(TxnsLogDO.class);
        criteria.add(Restrictions.eq("txnseqno", txnseqno));
        TxnsLogDO txnsLog = (TxnsLogDO) criteria.uniqueResult();
        if (txnsLog != null) {
            getSession().evict(txnsLog);
        }
        return txnsLog;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void updateTxnsLog(TxnsLogDO txnsLog) {
        update(txnsLog);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void updateTxnsLogRespInfo(TxnsLogDO txnsLog) {
        String hql = "update TxnsLogDO set payretcode = ? , payretinfo = ? ,accsettledate=? where txnseqno=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, txnsLog.getPayretcode());
        query.setString(1, txnsLog.getPayretinfo());
        query.setString(2, txnsLog.getAccsettledate());
        query.setString(3, txnsLog.getTxnseqno());
        int rows = query.executeUpdate();
        logger.info("updateTxnsLogRespInfo() effect rows:" + rows);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void saveTxnsLog(TxnsLogDO txnsLog) {
        saveEntity(txnsLog);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updatePayInfo(String txnseqno, String txid, String senderOrgCode) {
        String hql = "update TxnsLogDO set payordno=?,payinst=?,payfirmerno=?,payordcomtime=? where txnseqno=?";
        Query query = getSession().createQuery(hql);
        query.setParameter(0, txid);
        query.setParameter(1, ChannelCode.CHL_HZQSZX.getValue());
        query.setParameter(2, senderOrgCode);
        query.setParameter(3, DateUtil.getCurrentDateTime());
        query.setParameter(4, txnseqno);
        int rows = query.executeUpdate();
        logger.info("updatePayInfo() effect rows:" + rows);
    }

}
