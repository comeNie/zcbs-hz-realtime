package org.hz.realtime.business.message.dao;

import java.util.Map;

import org.hz.realtime.business.message.pojo.TTxnsLogDO;

import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;

/**
 * 交易流水表
 * 
 * @author AlanMa
 *
 */
public interface TxnsLogDAO extends BaseDAO<TTxnsLogDO> {

    /**
     * 根据交易序列号获取交易日志数据
     * 
     * @param txnseqno
     *            交易序列号
     * @return
     */
    public TTxnsLogDO getTxnsLogByTxnseqno(String txnseqno);

    /**
     * 更新交易流水日志数据
     * 
     * @param txnsLog
     *            交易流水日志pojo
     */
    public void updateTxnsLog(TTxnsLogDO txnsLog);

    /**
     * 更新交易流水日志数据
     * 
     * @param txnsLog
     *            交易流水日志pojo
     */
    public void updateTxnsLogRespInfo(TTxnsLogDO txnsLog);

    /**
     * 保存交易日志
     * 
     * @param txnsLog
     *            交易日志pojo
     */
    public void saveTxnsLog(TTxnsLogDO txnsLog);
}
