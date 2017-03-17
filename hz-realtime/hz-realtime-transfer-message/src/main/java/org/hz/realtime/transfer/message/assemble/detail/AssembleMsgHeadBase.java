package org.hz.realtime.transfer.message.assemble.detail;

import org.hz.realtime.transfer.message.api.bean.MessageHeaderBean;

public interface AssembleMsgHeadBase {

    /**
     * 生成报文头
     * 
     * @param bean
     * @return
     */
    public String createMessageHead(MessageHeaderBean beanHead);

}
