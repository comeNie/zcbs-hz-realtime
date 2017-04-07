package com.zcbspay.platform.hz.realtime.fe.net.netty.remote.impl;

import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.business.message.service.BusinessMessageReceiver;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.fe.net.netty.remote.RemoteAdapter;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.MessageRespBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.exception.HZRealTransferException;
import com.zcbspay.platform.hz.realtime.transfer.message.api.unpack.MessageUnpack;

@Service("remoteAdapterHZ")
public class RemoteAdapterImpl implements RemoteAdapter {

    @com.alibaba.dubbo.config.annotation.Reference(version = "1.0")
    private MessageUnpack messageUnpack;

    @com.alibaba.dubbo.config.annotation.Reference(version = "1.0")
    private BusinessMessageReceiver businessMessageReceiver;

    @Override
    public com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageRespBean unpack(byte[] headInfo, byte[] signInfo, byte[] bodyInfo) throws HZRealTransferException {
        return messageUnpack.unpack(headInfo, signInfo, bodyInfo);
    }

    @Override
    public ResultBean realTimeCollectionChargesReceipt(MessageRespBean messageRespBean) {
        return businessMessageReceiver.realTimeCollectionChargesReceipt(messageRespBean);
    }

    @Override
    public ResultBean realTimePaymentReceipt(MessageRespBean messageRespBean) {
        return businessMessageReceiver.realTimePaymentReceipt(messageRespBean);
    }

    @Override
    public ResultBean discardMessage(MessageRespBean messageRespBean) {
        return businessMessageReceiver.discardMessage(messageRespBean);
    }

    @Override
    public ResultBean busStaQryResp(MessageRespBean messageRespBean) {
        return businessMessageReceiver.busStaQryResp(messageRespBean);
    }

    @Override
    public ResultBean commProcAfrmResp(com.zcbspay.platform.hz.realtime.business.message.service.bean.MessageRespBean messageRespBean) {
        return businessMessageReceiver.commProcAfrmResp(messageRespBean);
    }

}
