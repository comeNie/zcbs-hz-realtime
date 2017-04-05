package com.zcbspay.platform.hz.realtime.fe.net.netty.remote;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.MessageRespBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.exception.HZRealTransferException;


/**
 * 远程调用其他工程适配器
 * 
 * @author AlanMa
 *
 */
public interface RemoteAdapter {

    /**
     * 解析回执报文
     * 
     * @param msgInfo
     * @return
     */
    public com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageRespBean unpack(byte[] headInfo, byte[] signInfo, byte[] bodyInfo) throws HZRealTransferException;

    /**
     * 实时代收回执报文处理方法
     * 
     * @param messageRespBean
     * @return
     */
    public ResultBean realTimeCollectionChargesReceipt(MessageRespBean messageRespBean);

    /**
     * 实时代付回执报文处理方法
     * 
     * @param messageRespBean
     * @return
     */
    public ResultBean realTimePaymentReceipt(MessageRespBean messageRespBean);

    /**
     * 丢弃报文处理方法
     * 
     * @param messageRespBean
     * @return
     */
    public ResultBean discardMessage(MessageRespBean messageRespBean);

    /**
     * 业务状态查询应答
     * 
     * @param messageRespBean
     * @return
     */
    public ResultBean busStaQryResp(MessageRespBean messageRespBean);

    /**
     * 通用处理确认应答
     * 
     * @param messageRespBean
     * @return
     */
    public ResultBean commProcAfrmResp(MessageRespBean messageRespBean);

    /**
     * 探测回应报文
     * 
     * @param messageRespBean
     * @return
     */
    public ResultBean detectResponse(MessageRespBean messageRespBean);

}
