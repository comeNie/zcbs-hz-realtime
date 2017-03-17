package com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail;

import com.zcbspay.platform.hz.realtime.common.bean.MessageHeaderBean;

public interface AssembleMsgHeadBase {

    /**
     * 生成报文头
     * 
     * @param bean
     * @return
     */
    public String createMessageHead(MessageHeaderBean beanHead);

}
