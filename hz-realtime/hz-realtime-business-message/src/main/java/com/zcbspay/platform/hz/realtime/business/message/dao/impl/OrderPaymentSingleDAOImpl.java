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

import com.zcbspay.platform.hz.realtime.business.message.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.OrderPaymentSingleDO;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;

@Repository
public class OrderPaymentSingleDAOImpl extends HibernateBaseDAOImpl<OrderPaymentSingleDO> implements OrderPaymentSingleDAO {

    private static final Logger log = LoggerFactory.getLogger(OrderPaymentSingleDAOImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateOrderToFail(String txnseqno) {
        String hql = "update OrderPaymentSingleDO set status = ? where relatetradetxn = ? ";
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
        String hql = "update OrderPaymentSingleDO set status = ? where relatetradetxn = ? ";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, "00");
        query.setString(1, txnseqno);
        int rows = query.executeUpdate();
        log.info("updateOrderToSuccess() effect rows:" + rows);
    }

    /**
     *
     * @param tn
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateOrderToSuccessByTN(String tn) {
        // TODO Auto-generated method stub
        String hql = "update OrderPaymentSingleDO set status = ? where tn = ? ";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, "00");
        query.setString(1, tn);
        int rows = query.executeUpdate();
        log.info("updateOrderToSuccessByTN() effect rows:" + rows);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPaymentSingleDO getPaymentSingleOrderByTN(String tn) {
        Criteria criteria = getSession().createCriteria(OrderPaymentSingleDO.class);
        criteria.add(Restrictions.eq("tn", tn));
        OrderPaymentSingleDO uniqueResult = (OrderPaymentSingleDO) criteria.uniqueResult();
        return uniqueResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void updateOrderToFailByTn(String tn) {
        String hql = "update OrderPaymentSingleDO set status = ? where tn = ? ";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, "03");
        query.setString(1, tn);
        int rows = query.executeUpdate();
        log.info("updateOrderToFail() effect rows:" + rows);

    }

}
