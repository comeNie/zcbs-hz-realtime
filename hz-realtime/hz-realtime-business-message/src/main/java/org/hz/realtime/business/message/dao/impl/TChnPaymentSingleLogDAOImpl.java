package org.hz.realtime.business.message.dao.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import org.hz.realtime.business.message.pojo.TChnPaymentSingleLogDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateStyle;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateTimeUtils;
import com.zcbspay.platform.hz.realtime.message.bean.CMT387Bean;

@Repository
public class TChnPaymentSingleLogDAOImpl extends HibernateBaseDAOImpl<TChnPaymentSingleLogDO> implements TChnPaymentSingleLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(TChnPaymentSingleLogDAOImpl.class);
    @Resource
    private SerialNumberService redisSerialNumberService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public TChnPaymentSingleLogDO saveRealPaymentLog(SinglePaymentBean paymentBean, String msgId) {
        // 记录实时代付流水(T_CHN_PAYMENT_SINGLE_LOG)
        TChnPaymentSingleLogDO chnPaymentSingleLog = new TChnPaymentSingleLogDO();
        chnPaymentSingleLog.setTid(redisSerialNumberService.generateDBPrimaryKey());
        chnPaymentSingleLog.setMsgid(msgId);
        chnPaymentSingleLog.setTxid(paymentBean.getTxId());
        chnPaymentSingleLog.setDebtorname(paymentBean.getDebtorName());
        chnPaymentSingleLog.setDebtoraccountno(paymentBean.getDebtorAccountNo());
        chnPaymentSingleLog.setCreditorbranchcode(paymentBean.getCreditorBranchCode());
        chnPaymentSingleLog.setCreditorname(paymentBean.getCreditorName());
        chnPaymentSingleLog.setCreditoraccountno(paymentBean.getCreditorAccountNo());
        chnPaymentSingleLog.setAmount(Long.parseLong(paymentBean.getAmount()));
        chnPaymentSingleLog.setPurposeproprietary(paymentBean.getPurposeCode());
        chnPaymentSingleLog.setEndtoendidentification(paymentBean.getEndToEndIdentification());
        chnPaymentSingleLog.setTxnseqno(paymentBean.getTxnseqno());
        saveEntity(chnPaymentSingleLog);

        return chnPaymentSingleLog;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public TChnPaymentSingleLogDO updateRealPaymentLog(CMT387Bean realTimePayRespBean) {
        String hql = "from TChnPaymentSingleLogDAO where msgId=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, realTimePayRespBean.getOrgnlMsgId().getOrgnlMsgId());
        TChnPaymentSingleLogDO paymentSingle = (TChnPaymentSingleLogDO) query.uniqueResult();
        paymentSingle.setRspmsgid(realTimePayRespBean.getMsgId());
        paymentSingle.setRspstatus(realTimePayRespBean.getRspnInf().getSts());
        paymentSingle.setRsprejectcode(realTimePayRespBean.getRspnInf().getRjctcd());
        paymentSingle.setRsprejectinformation(realTimePayRespBean.getRspnInf().getNetgdt());
        paymentSingle.setRspdate(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        paymentSingle.setNettingdate(realTimePayRespBean.getRspnInf().getNetgdt());
        TChnPaymentSingleLogDO retDo = update(paymentSingle);
        return retDo;
    }

    @Override
    public TChnPaymentSingleLogDO getPaySingleByTxnseqno(String txnseqno) {
        String hql = "from TChnPaymentSingleLogDO where txnseqno=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, txnseqno);
        return (TChnPaymentSingleLogDO) query.uniqueResult();
    }

}
