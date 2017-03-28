package com.zcbspay.platform.hz.realtime.business.message.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.hz.realtime.business.message.dao.TxnsLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TTxnsLogDO;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;

@Repository("txnsLogDAO")
public class TxnsLogDAOImpl extends HibernateBaseDAOImpl<TTxnsLogDO> implements TxnsLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(TxnsLogDAOImpl.class);

    @Override
    public TTxnsLogDO getTxnsLogByTxnseqno(String txnseqno) {
        Criteria criteria = getSession().createCriteria(TTxnsLogDO.class);
        criteria.add(Restrictions.eq("txnseqno", txnseqno));
        TTxnsLogDO txnsLog = (TTxnsLogDO) criteria.uniqueResult();
        if (txnsLog != null) {
            getSession().evict(txnsLog);
        }
        return txnsLog;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateTxnsLog(TTxnsLogDO txnsLog) {
        update(txnsLog);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateTxnsLogRespInfo(TTxnsLogDO txnsLog) {
        String hql = "update TTxnsLogDO set payretcode = ? , payretinfo = ? ,accsettledate=? where txnseqno=?";
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
    public void saveTxnsLog(TTxnsLogDO txnsLog) {
        saveEntity(txnsLog);
    }

}
