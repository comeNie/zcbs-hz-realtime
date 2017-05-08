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
import com.zcbspay.platform.hz.realtime.business.message.enums.HZRspStatus;
import com.zcbspay.platform.hz.realtime.business.message.enums.OrgCode;
import com.zcbspay.platform.hz.realtime.business.message.pojo.ChnPaymentSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateStyle;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateTimeUtils;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateUtil;
import com.zcbspay.platform.hz.realtime.message.bean.CMS317Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS911Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT387Bean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;

@Repository
public class TChnPaymentSingleLogDAOImpl extends HibernateBaseDAOImpl<ChnPaymentSingleLogDO> implements TChnPaymentSingleLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(TChnPaymentSingleLogDAOImpl.class);
    @Resource(name = "redisSerialNumberService")
    private SerialNumberService redisSerialNumberService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public ChnPaymentSingleLogDO saveRealPaymentLog(SinglePaymentBean paymentBean, String msgId, String comRefId, String senderOrgCode) {
        // 记录实时代付流水(T_CHN_PAYMENT_SINGLE_LOG)
        ChnPaymentSingleLogDO chnPaymentSingleLog = new ChnPaymentSingleLogDO();
        chnPaymentSingleLog.setTid(redisSerialNumberService.generateDBPrimaryKey());
        chnPaymentSingleLog.setMsgid(msgId);
        chnPaymentSingleLog.setTxid(paymentBean.getTxId());
        chnPaymentSingleLog.setTransdate(DateUtil.getCurrentDate());
        chnPaymentSingleLog.setTranstime(DateUtil.getCurrentTime());
        chnPaymentSingleLog.setDebtorname(paymentBean.getDebtorName());
        chnPaymentSingleLog.setDebtoraccountno(paymentBean.getDebtorAccountNo());
        chnPaymentSingleLog.setDebtorbranchcode(paymentBean.getDebtorAgentCode());
        chnPaymentSingleLog.setCreditorbranchcode(paymentBean.getCreditorAgentCode());
        chnPaymentSingleLog.setCreditorname(paymentBean.getCreditorName());
        chnPaymentSingleLog.setCreditoraccountno(paymentBean.getCreditorAccountNo());
        chnPaymentSingleLog.setAmount(Long.parseLong(paymentBean.getAmount()));
        chnPaymentSingleLog.setPurposeproprietary(paymentBean.getPurposeCode());
        chnPaymentSingleLog.setEndtoendidentification(paymentBean.getEndToEndIdentification());
        chnPaymentSingleLog.setTxnseqno(paymentBean.getTxnseqno());
        chnPaymentSingleLog.setRemarks(paymentBean.getSummary());
        chnPaymentSingleLog.setRspstatus(HZRspStatus.UNKNOWN.getValue());
        chnPaymentSingleLog.setCommunno(comRefId);
        chnPaymentSingleLog.setTradetype(MessageTypeEnum.CMT386.value());
        chnPaymentSingleLog.setTransmitleg(senderOrgCode);
        chnPaymentSingleLog.setReceiver(OrgCode.HZQSZX.getValue());
        saveEntity(chnPaymentSingleLog);

        return chnPaymentSingleLog;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public ChnPaymentSingleLogDO updateRealPaymentLog(CMT387Bean realTimePayRespBean) {
        String hql = "from ChnPaymentSingleLogDO where msgId=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, realTimePayRespBean.getOrgnlMsgId().getOrgnlMsgId());
        ChnPaymentSingleLogDO paymentSingle = (ChnPaymentSingleLogDO) query.uniqueResult();
        paymentSingle.setRspmsgid(realTimePayRespBean.getMsgId());
        paymentSingle.setRspstatus(realTimePayRespBean.getRspnInf().getSts());
        paymentSingle.setRsprejectcode(realTimePayRespBean.getRspnInf().getRjctcd());
        paymentSingle.setRsprejectinformation(realTimePayRespBean.getRspnInf().getRjctinf());
        paymentSingle.setRspdate(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        paymentSingle.setNettingdate(realTimePayRespBean.getRspnInf().getNetgdt());
        ChnPaymentSingleLogDO retDo = update(paymentSingle);
        return retDo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public ChnPaymentSingleLogDO updateRealPaymentLog(CMS317Bean queryBusStsRsp, long tid) {
        String hql = "from ChnPaymentSingleLogDO where tid=?";
        Query query = getSession().createQuery(hql);
        query.setLong(0, tid);
        ChnPaymentSingleLogDO paymentSingle = (ChnPaymentSingleLogDO) query.uniqueResult();
        paymentSingle.setRspmsgid(queryBusStsRsp.getMsgId());
        paymentSingle.setRspstatus(queryBusStsRsp.getRspnInf().getSts());
        paymentSingle.setRsprejectcode(queryBusStsRsp.getRspnInf().getRjctcd());
        paymentSingle.setRsprejectinformation(queryBusStsRsp.getRspnInf().getRjctinf());
        paymentSingle.setRspdate(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        paymentSingle.setNettingdate(queryBusStsRsp.getRspnInf().getNetgdt());
        ChnPaymentSingleLogDO retDo = update(paymentSingle);
        return retDo;
    }

    @Override
    @Transactional(readOnly = true)
    public ChnPaymentSingleLogDO getPaySingleByTxnseqnoNotFail(String txnseqno) {
        String hql = "from ChnPaymentSingleLogDO where txnseqno=? and rspstatus!=? and rspstatus!=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, txnseqno);
        query.setString(1, HZRspStatus.REJECTED.getValue());
        query.setString(2, HZRspStatus.OVERDUE.getValue());
        return (ChnPaymentSingleLogDO) query.uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public ChnPaymentSingleLogDO getPaySingleByTxnseqno(String txnseqno) {
        String hql = "from ChnPaymentSingleLogDO where txnseqno=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, txnseqno);
        return (ChnPaymentSingleLogDO) query.uniqueResult();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateRealPaymentLogCommResp(CMS900Bean bean) {

        String hql = null;
        if (HZRspStatus.TRANSFER.getValue().equals(bean.getRspnInf().getSts())) {
            hql = "update ChnPaymentSingleLogDO set commsgid = ? , comstatus = ? ,comrejectcode=? ,comrejectinformation=? ,comdate=? where msgid=?";
        }
        else {
            hql = "update ChnPaymentSingleLogDO set commsgid = ? , comstatus = ? ,comrejectcode=? ,comrejectinformation=? ,comdate=?, rspstatus=? where msgid=?";
        }
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, bean.getMsgId());
        query.setString(1, bean.getRspnInf().getSts());
        query.setString(2, bean.getRspnInf().getRjctcd());
        query.setString(3, bean.getRspnInf().getRjctinf());
        query.setString(4, DateUtil.getCurrentDateTime());
        if (HZRspStatus.TRANSFER.getValue().equals(bean.getRspnInf().getSts())) {
            query.setString(5, bean.getOrgnlMsgId().getOrgnlMsgId());
        }
        else {
            query.setString(5, HZRspStatus.REJECTED.getValue());
            query.setString(6, bean.getOrgnlMsgId().getOrgnlMsgId());
        }

        int rows = query.executeUpdate();
        logger.info("updateRealPaymentLogCommResp() effect rows:" + rows);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void updateRealPaymentLogDiscard(CMS911Bean bean) {
        String hql = "update ChnPaymentSingleLogDO set commsgid = ? ,comrejectcode=? ,comrejectinformation=? where communno=?";
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
    public ChnPaymentSingleLogDO getPaySingleByTid(long tid) {
        ChnPaymentSingleLogDO tChnPaymentSingleLogDO = (ChnPaymentSingleLogDO) getSession().get(ChnPaymentSingleLogDO.class, tid);
        return tChnPaymentSingleLogDO;
    }

    @Override
    @Transactional(readOnly = true)
    public ChnPaymentSingleLogDO getPaySingleByMsgId(String msgId) {
        String hql = "from ChnPaymentSingleLogDO where msgid=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, msgId);
        return (ChnPaymentSingleLogDO) query.uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public ChnPaymentSingleLogDO getCollSingleByTxId(String txId) {
        String hql = "from ChnPaymentSingleLogDO where txid=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, txId);
        return (ChnPaymentSingleLogDO) query.uniqueResult();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateRealPayLogSendInfo(long tid, String status, String errCodeMsg) {
        String hql = "update ChnPaymentSingleLogDO set rspstatus = ? ,comrejectinformation=? where tid=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, status);
        query.setString(1, errCodeMsg);
        query.setLong(2, tid);
        int rows = query.executeUpdate();
        logger.info("updateRealPayLogSendInfo() effect rows:" + rows);

    }

}
