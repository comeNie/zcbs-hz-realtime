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
import com.zcbspay.platform.hz.realtime.business.message.enums.HZRspStatus;
import com.zcbspay.platform.hz.realtime.business.message.pojo.ChnCollectSingleLogDO;
import com.zcbspay.platform.hz.realtime.business.message.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateStyle;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateTimeUtils;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateUtil;
import com.zcbspay.platform.hz.realtime.message.bean.CMS317Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS911Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT385Bean;

@Repository
public class TChnCollectSingleLogDAOImpl extends HibernateBaseDAOImpl<ChnCollectSingleLogDO> implements TChnCollectSingleLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(TChnCollectSingleLogDAOImpl.class);
    @Resource(name = "redisSerialNumberService")
    private SerialNumberService redisSerialNumberService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public ChnCollectSingleLogDO saveRealCollectLog(SingleCollectionChargesBean collectionChargesBean, String msgId, String comRefId) {
        // 记录实时代收流水(T_CHN_COLLECT_SINGLE_LOG)
        ChnCollectSingleLogDO chnCollectSingleLog = new ChnCollectSingleLogDO();
        chnCollectSingleLog.setTid(redisSerialNumberService.generateDBPrimaryKey());
        chnCollectSingleLog.setMsgid(msgId);
        chnCollectSingleLog.setTxid(collectionChargesBean.getTxId());
        chnCollectSingleLog.setTransdate(DateUtil.getCurrentDate());
        chnCollectSingleLog.setTranstime(DateUtil.getCurrentTime());
        chnCollectSingleLog.setDebtorname(collectionChargesBean.getDebtorName());
        chnCollectSingleLog.setDebtoraccountno(collectionChargesBean.getDebtorAccountNo());
        chnCollectSingleLog.setDebtorbranchcode(collectionChargesBean.getDebtorBranchCode());
        chnCollectSingleLog.setCreditorbranchcode(collectionChargesBean.getCreditorBranchCode());
        chnCollectSingleLog.setCreditorname(collectionChargesBean.getCreditorName());
        chnCollectSingleLog.setCreditoraccountno(collectionChargesBean.getCreditorAccountNo());
        chnCollectSingleLog.setAmount(Long.parseLong(collectionChargesBean.getAmount()));
        chnCollectSingleLog.setPurposeproprietary(collectionChargesBean.getPurposeCode());
        chnCollectSingleLog.setEndtoendidentification(collectionChargesBean.getEndToEndIdentification());
        chnCollectSingleLog.setSummary(collectionChargesBean.getSummary());
        chnCollectSingleLog.setTxnseqno(collectionChargesBean.getTxnseqno());
        chnCollectSingleLog.setRspstatus(HZRspStatus.UNKNOWN.getValue());
        // 借用Notes备注字段储存通讯级参考号,用于丢弃报文匹配原交易
        chnCollectSingleLog.setNotes(comRefId);

        saveEntity(chnCollectSingleLog);
        return chnCollectSingleLog;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public ChnCollectSingleLogDO updateRealCollectLog(CMT385Bean realTimeCollRespBean) {
        String hql = "from ChnCollectSingleLogDO where msgId=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, realTimeCollRespBean.getOrgnlMsgId().getOrgnlMsgId());
        ChnCollectSingleLogDO collSingle = (ChnCollectSingleLogDO) query.uniqueResult();
        collSingle.setRspmsgid(realTimeCollRespBean.getMsgId());
        collSingle.setRspstatus(realTimeCollRespBean.getRspnInf().getSts());
        collSingle.setRsprejectcode(realTimeCollRespBean.getRspnInf().getRjctcd());
        collSingle.setRsprejectinformation(realTimeCollRespBean.getRspnInf().getRjctinf());
        collSingle.setRspdate(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        collSingle.setNettingdate(realTimeCollRespBean.getRspnInf().getNetgdt());
        ChnCollectSingleLogDO retDo = update(collSingle);
        return retDo;
    }

    @Override
    @Transactional(readOnly = true)
    public ChnCollectSingleLogDO getCollSingleByTxnseqnoNotFail(String txnseqno) {
        String hql = "from ChnCollectSingleLogDO where txnseqno=? and rspstatus!=? and rspstatus!=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, txnseqno);
        query.setString(1, HZRspStatus.REJECTED.getValue());
        query.setString(2, HZRspStatus.OVERDUE.getValue());
        return (ChnCollectSingleLogDO) query.uniqueResult();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateRealCollectLogCommResp(CMS900Bean bean) {
        String hql = null;
        if (HZRspStatus.SUCCESS.getValue().equals(bean.getRspnInf().getSts())) {
            hql = "update ChnCollectSingleLogDO set commsgid = ? , comstatus = ? ,comrejectcode=? ,comrejectinformation=? ,comdate=? where msgid=?";
        }
        else {
            hql = "update ChnCollectSingleLogDO set commsgid = ? , comstatus = ? ,comrejectcode=? ,comrejectinformation=? ,comdate=?, rspstatus=? where msgid=?";
        }
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, bean.getMsgId());
        query.setString(1, bean.getRspnInf().getSts());
        query.setString(2, bean.getRspnInf().getRjctcd());
        query.setString(3, bean.getRspnInf().getRjctinf());
        query.setString(4, DateUtil.getCurrentDateTime());
        if (HZRspStatus.SUCCESS.getValue().equals(bean.getRspnInf().getSts())) {
            query.setString(5, bean.getOrgnlMsgId().getOrgnlMsgId());
        }
        else {
            query.setString(5, HZRspStatus.REJECTED.getValue());
            query.setString(6, bean.getOrgnlMsgId().getOrgnlMsgId());
        }

        int rows = query.executeUpdate();
        logger.info("updateRealCollectLogCommResp() effect rows:" + rows);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void updateRealCollectLogDiscard(CMS911Bean bean) {
        String hql = "update ChnCollectSingleLogDO set commsgid = ? ,comrejectcode=? ,comrejectinformation=? where notes=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, bean.getMsgId());
        query.setString(1, bean.getDscrdInf().getRjctCd());
        query.setString(2, bean.getDscrdInf().getRjctInf());
        query.setString(3, bean.getDscrdInf().getRefId());
        int rows = query.executeUpdate();
        logger.info("updateRealCollectLogDiscard() effect rows:" + rows);

    }

    @Override
    @Transactional(readOnly = true)
    public ChnCollectSingleLogDO getCollSingleByTid(long tid) {
        ChnCollectSingleLogDO tChnCollectSingleLogDO = (ChnCollectSingleLogDO) getSession().get(ChnCollectSingleLogDO.class, tid);
        return tChnCollectSingleLogDO;
    }

    @Override
    @Transactional(readOnly = true)
    public ChnCollectSingleLogDO getCollSingleByMsgId(String msgId) {
        String hql = "from ChnCollectSingleLogDO where msgid=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, msgId);
        return (ChnCollectSingleLogDO) query.uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public ChnCollectSingleLogDO getCollSingleByTxId(String txId) {
        String hql = "from ChnCollectSingleLogDO where txid=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, txId);
        return (ChnCollectSingleLogDO) query.uniqueResult();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public ChnCollectSingleLogDO updateRealCollectLog(CMS317Bean queryBusStsRsp, long tid) {
        String hql = "from ChnCollectSingleLogDO where tid=?";
        Query query = getSession().createQuery(hql);
        query.setLong(0, tid);
        ChnCollectSingleLogDO collectSingle = (ChnCollectSingleLogDO) query.uniqueResult();
        collectSingle.setRspmsgid(queryBusStsRsp.getMsgId());
        collectSingle.setRspstatus(queryBusStsRsp.getRspnInf().getSts());
        collectSingle.setRsprejectcode(queryBusStsRsp.getRspnInf().getRjctcd());
        collectSingle.setRsprejectinformation(queryBusStsRsp.getRspnInf().getRjctinf());
        collectSingle.setRspdate(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        collectSingle.setNettingdate(queryBusStsRsp.getRspnInf().getNetgdt());
        ChnCollectSingleLogDO retDo = update(collectSingle);
        return retDo;
    }

    @Override
    @Transactional(readOnly = true)
    public ChnCollectSingleLogDO getCollSingleByTxnseqnoAndRspSta(String txnseqno, String... rspStatus) {
        String hql = "from ChnCollectSingleLogDO where txnseqno=? and rspstatus in (?,?)";
        Query query = getSession().createQuery(hql);
        query.setString(0, txnseqno);
        query.setString(1, rspStatus[0]);
        query.setString(2, rspStatus[1]);
        return (ChnCollectSingleLogDO) query.uniqueResult();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateRealCollLogSendInfo(long tid, String status, String errCodeMsg) {
        String hql = "update ChnCollectSingleLogDO set rspstatus = ? ,comrejectinformation=? where tid=?";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString(0, status);
        query.setString(1, errCodeMsg);
        query.setLong(2, tid);
        int rows = query.executeUpdate();
        logger.info("updateRealCollLogSendInfo() effect rows:" + rows);
    }

}
