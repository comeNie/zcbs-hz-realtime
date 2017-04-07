package com.zcbspay.platform.hz.realtime.message.bean.fe.service;

import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBeanStr;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.exception.HZRealFeException;

public interface MessageSend {

    /**
     * 发送报文至第三方
     * 
     * @param messageBean
     * @return
     * @throws HZRealFeException
     */
    public String sendMessage(MessageBeanStr messageBean) throws HZRealFeException;

}
