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
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public int updateOrderStatus(String txnseqno, String status) {
        String hql = "update OrderPaymentSingleDO set status = ? where relatetradetxn = ? ";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, status);
        query.setString(1, txnseqno);
        int rows = query.executeUpdate();
        log.info("updateOrderToSuccess() effect rows:" + rows);
        return rows;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPaymentSingleDO getPaymentSingleOrderByTN(String tn) {
        Criteria criteria = getSession().createCriteria(OrderPaymentSingleDO.class);
        criteria.add(Restrictions.eq("tn", tn));
        OrderPaymentSingleDO uniqueResult = (OrderPaymentSingleDO) criteria.uniqueResult();
        return uniqueResult;
    }

}
