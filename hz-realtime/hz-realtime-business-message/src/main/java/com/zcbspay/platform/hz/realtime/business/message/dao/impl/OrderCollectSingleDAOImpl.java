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

import com.zcbspay.platform.hz.realtime.business.message.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;

@Repository
public class OrderCollectSingleDAOImpl extends HibernateBaseDAOImpl<OrderCollectSingleDO> implements OrderCollectSingleDAO {

    private static final Logger log = LoggerFactory.getLogger(OrderCollectSingleDAOImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateOrderToFail(String txnseqno) {
        String hql = "update OrderCollectSingleDO set status = ? where relatetradetxn = ? ";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, "03");
        query.setString(1, txnseqno);
        int rows = query.executeUpdate();
        log.info("updateOrderToFail() effect rows:" + rows);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void updateOrderToSuccess(String txnseqno) {
        String hql = "update OrderCollectSingleDO set status = ? where relatetradetxn = ? ";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, "00");
        query.setString(1, txnseqno);
        int rows = query.executeUpdate();
        log.info("updateOrderToSuccess() effect rows:" + rows);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderCollectSingleDO getCollectSingleOrderByTN(String tn) {
        Criteria criteria = getSession().createCriteria(OrderCollectSingleDO.class);
        criteria.add(Restrictions.eq("tn", tn));
        OrderCollectSingleDO uniqueResult = (OrderCollectSingleDO) criteria.uniqueResult();
        return uniqueResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateOrderToFailByTn(String tn) {
        String hql = "update OrderCollectSingleDO set status = ? where tn = ? ";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, "03");
        query.setString(1, tn);
        int rows = query.executeUpdate();
        log.info("updateOrderToFail() effect rows:" + rows);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void updateOrderToSuccessByTn(String tn) {
        String hql = "update OrderCollectSingleDO set status = ? where tn = ? ";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, "00");
        query.setString(1, tn);
        int rows = query.executeUpdate();
        log.info("updateOrderToSuccess() effect rows:" + rows);
    }

}
