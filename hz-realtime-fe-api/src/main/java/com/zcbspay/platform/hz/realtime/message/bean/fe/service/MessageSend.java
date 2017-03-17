package com.zcbspay.platform.hz.realtime.message.bean.fe.service;

import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.SendResult;

public interface MessageSend {

    /**
     * 发送报文至第三方
     * 
     * @param messageBean
     * @return
     */
    public void sendMessage(MessageBean messageBean);

}
