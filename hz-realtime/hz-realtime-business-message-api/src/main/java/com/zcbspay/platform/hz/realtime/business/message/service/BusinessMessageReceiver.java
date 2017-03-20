package com.zcbspay.platform.hz.realtime.business.message.service;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.MessageRespBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;


/**
 * 业务报文接收器
 *
 * @author guojia
 * @version
 * @date 2017年3月9日 下午4:07:57
 * @since
 */
public interface BusinessMessageReceiver {

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
