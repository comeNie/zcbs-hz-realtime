package com.zcbspay.platform.hz.realtime.message.bean.fe.service;


import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBeanStr;


public interface MessageSend {

    /**
     * 发送报文至第三方
     * 
     * @param messageBean
     * @return
     */
    public void sendMessage(MessageBeanStr messageBean);


}
