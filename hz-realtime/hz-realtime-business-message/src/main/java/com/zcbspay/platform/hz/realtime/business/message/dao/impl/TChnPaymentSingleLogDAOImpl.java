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

import com.zcbspay.platform.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TChnPaymentSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateStyle;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateTimeUtils;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateUtil;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS911Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT387Bean;

@Repository
public class TChnPaymentSingleLogDAOImpl extends HibernateBaseDAOImpl<TChnPaymentSingleLogDO> implements TChnPaymentSingleLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(TChnPaymentSingleLogDAOImpl.class);
    @Resource(name = "redisSerialNumberService")
    private SerialNumberService redisSerialNumberService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public TChnPaymentSingleLogDO saveRealPaymentLog(SinglePaymentBean paymentBean, String msgId, String comRefId) {
        // 记录实时代付流水(T_CHN_PAYMENT_SINGLE_LOG)
        TChnPaymentSingleLogDO chnPaymentSingleLog = new TChnPaymentSingleLogDO();
        chnPaymentSingleLog.setTid(redisSerialNumberService.generateDBPrimaryKey());
        chnPaymentSingleLog.setMsgid(msgId);
        chnPaymentSingleLog.setTxid(paymentBean.getTxId());
        chnPaymentSingleLog.setTransdate(DateUtil.getCurrentDate());
        chnPaymentSingleLog.setTranstime(DateUtil.getCurrentTime());
        chnPaymentSingleLog.setDebtorname(paymentBean.getDebtorName());
        chnPaymentSingleLog.setDebtoraccountno(paymentBean.getDebtorAccountNo());
        chnPaymentSingleLog.setDebtorbranchcode(paymentBean.getDebtorBranchCode());
        chnPaymentSingleLog.setCreditorbranchcode(paymentBean.getCreditorBranchCode());
        chnPaymentSingleLog.setCreditorname(paymentBean.getCreditorName());
        chnPaymentSingleLog.setCreditoraccountno(paymentBean.getCreditorAccountNo());
        chnPaymentSingleLog.setAmount(Long.parseLong(paymentBean.getAmount()));
        chnPaymentSingleLog.setPurposeproprietary(paymentBean.getPurposeCode());
        chnPaymentSingleLog.setEndtoendidentification(paymentBean.getEndToEndIdentification());
        chnPaymentSingleLog.setTxnseqno(paymentBean.getTxnseqno());
        chnPaymentSingleLog.setRemarks(paymentBean.getSummary());
        // 借用Notes备注字段储存通讯级参考号,用于丢弃报文匹配原交易
        chnPaymentSingleLog.setNotes(comRefId);
        saveEntity(chnPaymentSingleLog);

        return chnPaymentSingleLog;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public TChnPaymentSingleLogDO updateRealPaymentLog(CMT387Bean realTimePayRespBean) {
        String hql = "from TChnPaymentSingleLogDO where msgId=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, realTimePayRespBean.getOrgnlMsgId().getOrgnlMsgId());
        TChnPaymentSingleLogDO paymentSingle = (TChnPaymentSingleLogDO) query.uniqueResult();
        paymentSingle.setRspmsgid(realTimePayRespBean.getMsgId());
        paymentSingle.setRspstatus(realTimePayRespBean.getRspnInf().getSts());
        paymentSingle.setRsprejectcode(realTimePayRespBean.getRspnInf().getRjctcd());
        paymentSingle.setRsprejectinformation(realTimePayRespBean.getRspnInf().getRjctinf());
        paymentSingle.setRspdate(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        paymentSingle.setNettingdate(realTimePayRespBean.getRspnInf().getNetgdt());
        TChnPaymentSingleLogDO retDo = update(paymentSingle);
        return retDo;
    }

    @Override
    @Transactional(readOnly = true)
    public TChnPaymentSingleLogDO getPaySingleByTxnseqno(String txnseqno) {
        String hql = "from TChnPaymentSingleLogDO where txnseqno=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, txnseqno);
        return (TChnPaymentSingleLogDO) query.uniqueResult();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateRealPaymentLogCommResp(CMS900Bean bean) {
        String hql = "update TChnPaymentSingleLogDO set commsgid = ? , comstatus = ? ,comrejectcode=? ,comrejectinformation=? ,comdate=? where msgid=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, bean.getMsgId());
        query.setString(1, bean.getRspnInf().getSts());
        query.setString(2, bean.getRspnInf().getRjctcd());
        query.setString(3, bean.getRspnInf().getRjctinf());
        query.setString(4, DateUtil.getCurrentDateTime());
        query.setString(5, bean.getOrgnlMsgId().getOrgnlMsgId());
        int rows = query.executeUpdate();
        logger.info("updateRealPaymentLogCommResp() effect rows:" + rows);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void updateRealPaymentLogDiscard(CMS911Bean bean) {
        String hql = "update TChnPaymentSingleLogDO set commsgid = ? ,comrejectcode=? ,comrejectinformation=? where notes=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, bean.getMsgId());
        query.setString(1, bean.getDscrdInf().getRjctCd());
        query.setString(2, bean.getDscrdInf().getRjctInf());
        query.setString(3, bean.getDscrdInf().getRefId());
        int rows = query.executeUpdate();
        logger.info("updateRealPaymentLogDiscard() effect rows:" + rows);
    }

    @Override
    @Transactional(readOnly = true)
    public TChnPaymentSingleLogDO getPaySingleByTid(long tid) {
        TChnPaymentSingleLogDO tChnPaymentSingleLogDO = (TChnPaymentSingleLogDO) getSession().get(TChnPaymentSingleLogDO.class, tid);
        return tChnPaymentSingleLogDO;
    }

}
