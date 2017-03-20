package com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail;

import com.zcbspay.platform.hz.realtime.common.exception.HZQSZXException;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;

public interface AssembleSignBase {

    /**
     * 根据报文bean以及报文的类型，对报文中的关键要素进行签名串的拼接
     * 
     * @param bean
     * @return
     */
    public String signatureElement(MessageBean bean) throws HZQSZXException;
}
