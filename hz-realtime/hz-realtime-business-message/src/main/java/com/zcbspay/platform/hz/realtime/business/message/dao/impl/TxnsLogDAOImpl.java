package com.zcbspay.platform.hz.realtime.business.message.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.hz.realtime.business.message.bean.TransLogUpBean;
import com.zcbspay.platform.hz.realtime.business.message.dao.RspmsgDAO;
import com.zcbspay.platform.hz.realtime.business.message.dao.TxnsLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.enums.BusinessType;
import com.zcbspay.platform.hz.realtime.business.message.enums.TradeStatFlagEnum;
import com.zcbspay.platform.hz.realtime.business.message.enums.TradeTxnFlagEnum;
import com.zcbspay.platform.hz.realtime.business.message.pojo.RspmsgDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TxnsLogDO;
import com.zcbspay.platform.hz.realtime.business.message.service.enums.ChnlTypeEnum;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.hz.realtime.common.utils.UUIDUtil;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateUtil;

@Repository("txnsLogDAO")
public class TxnsLogDAOImpl extends HibernateBaseDAOImpl<TxnsLogDO> implements TxnsLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(TxnsLogDAOImpl.class);

    @Autowired
    private RspmsgDAO rspmsgDAO;

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
    public void updatePayInfoResult(TransLogUpBean orderUpdateBean) {
        RspmsgDO rspmsg = rspmsgDAO.getRspmsgByChnlCode(ChnlTypeEnum.HZ, orderUpdateBean.getPayretcode());
        if (rspmsg == null) {
            rspmsg = new RspmsgDO();
            rspmsg.setRspinfo("未知异常");
        }
        String hql = "update TxnsLogDO set payordfintime = ?, payretcode = ?, payretinfo = ?, accordfintime = ?,retdatetime=?,tradetxnflag=?,tradestatflag = ?,relate=?,tradeseltxn=?,retcode = ?,retinfo = ?  where txnseqno=?";
        Query query = getSession().createQuery(hql);
        query.setParameter(0, DateUtil.getCurrentDateTime());
        query.setParameter(1, orderUpdateBean.getPayretcode());
        query.setParameter(2, rspmsg.getRspinfo());
        query.setParameter(3, DateUtil.getCurrentDateTime());
        query.setParameter(4, DateUtil.getCurrentDateTime());
        if (BusinessType.REAL_TIME_COLL == orderUpdateBean.getBusinessType()) {
            query.setParameter(5, TradeTxnFlagEnum.HZ_REALTIME_COLL.getCode());
        }
        else if (BusinessType.REAL_TIME_PAY == orderUpdateBean.getBusinessType()) {
            query.setParameter(5, TradeTxnFlagEnum.HZ_REALTIME_PAY.getCode());
        }
        query.setParameter(6, orderUpdateBean.getRspStatus());
        query.setParameter(7, "10000000");
        query.setParameter(8, UUIDUtil.uuid());
        query.setParameter(9, rspmsg.getWebrspcode());
        query.setParameter(10, rspmsg.getRspinfo());
        query.setParameter(11, orderUpdateBean.getTxnseqno());
        int rows = query.executeUpdate();
        logger.info("updatePayInfo() effect rows:" + rows);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updatePayInfo(String txnseqno, String txid, String senderOrgCode, String payInst) {
        String hql = "update TxnsLogDO set payordno=?,payinst=?,payfirmerno=?,payordcomtime=?,tradestatflag=? where txnseqno=?";
        Query query = getSession().createQuery(hql);
        query.setParameter(0, txid);
        query.setParameter(1, payInst);
        query.setParameter(2, senderOrgCode);
        query.setParameter(3, DateUtil.getCurrentDateTime());
        query.setParameter(4, TradeStatFlagEnum.PAYING.getStatus());
        query.setParameter(5, txnseqno);
        int rows = query.executeUpdate();
        logger.info("updatePayInfo() effect rows:" + rows);
    }

}
