package org.hz.realtime.business.message.dao.impl;

import javax.annotation.Resource;

import org.hz.realtime.business.message.bean.RealTimeCollRespBean;
import org.hz.realtime.business.message.bean.RealTimePayRespBean;
import org.hz.realtime.business.message.dao.TChnPaymentSingleLogDAO;
import org.hz.realtime.business.message.pojo.TChnCollectSingleLogDO;
import org.hz.realtime.business.message.pojo.TChnPaymentSingleLogDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;

@Repository
public class TChnPaymentSingleLogDAOImpl extends HibernateBaseDAOImpl<TChnPaymentSingleLogDO> implements TChnPaymentSingleLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(TChnPaymentSingleLogDAOImpl.class);
    @Resource
    private SerialNumberService redisSerialNumberService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public TChnPaymentSingleLogDO saveRealPaymentLog(SinglePaymentBean paymentBean) {
        // 记录实时代付流水(T_CHN_PAYMENT_SINGLE_LOG)
        TChnPaymentSingleLogDO chnPaymentSingleLog = new TChnPaymentSingleLogDO();
        chnPaymentSingleLog.setTid(redisSerialNumberService.generateDBPrimaryKey());
        chnPaymentSingleLog.setMsgid(redisSerialNumberService.generateHZMsgId());
        chnPaymentSingleLog.setTxid(redisSerialNumberService.generateTranIden());
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
    public TChnPaymentSingleLogDO updateRealPaymentLog(RealTimePayRespBean realTimePayRespBean) {
        // TODO mxwtodo
        return null;
    }

}
