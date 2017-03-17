package com.zcbspay.platform.hz.realtime.transfer.message.api.unpack;

import com.zcbspay.platform.hz.realtime.common.bean.MessageRespBean;

/**
 * 报文解析
 * 
 * @author alanma
 * @version
 * @date 2017年2月23日 下午5:54:29
 * @since
 */
public interface MessageUnpack {


    /**
     * 解析回执报文
     * @param msgInfo
     * @return
     */
    public MessageRespBean unpack(byte[] msgInfo);

}
