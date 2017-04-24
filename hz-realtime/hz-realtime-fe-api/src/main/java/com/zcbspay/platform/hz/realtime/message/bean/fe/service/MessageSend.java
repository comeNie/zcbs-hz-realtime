package com.zcbspay.platform.hz.realtime.message.bean.fe.service;

import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBeanStr;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.exception.HZRealFeException;

public interface MessageSend {

    /**
     * 发送报文至第三方
     * 
     * @param messageBean
     * @return
     * @throws HZRealFeException
     */
    public ResultBean sendMessage(MessageBeanStr messageBean) throws HZRealFeException;

}
