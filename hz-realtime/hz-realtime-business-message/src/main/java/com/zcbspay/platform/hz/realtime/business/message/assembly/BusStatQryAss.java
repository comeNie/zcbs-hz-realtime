package com.zcbspay.platform.hz.realtime.business.message.assembly;

import javax.annotation.Resource;

import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.message.bean.CMS316Bean;
import com.zcbspay.platform.hz.realtime.message.bean.OrgnlTxBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.MessageTypeEnum;

/**
 * 业务状态查询报文（CMS316）
 *
 * @author alanma
 * @version
 * @date 2017年3月6日 上午9:52:07
 * @since
 */
public class BusStatQryAss {

    @Resource
    private static SerialNumberService redisSerialNumberService;

    public static MessageBean busStatusQryMsgBodyReq(OrgnlTxBean orgMsgIde) {
        MessageBean msgBean = new MessageBean();
        msgBean.setMessageTypeEnum(MessageTypeEnum.CMS316);
        CMS316Bean bean = new CMS316Bean();
        bean.setMsgId(redisSerialNumberService.generateHZMsgId());
        bean.setOrgnlTx(orgMsgIde);
        msgBean.setMessageBean(bean);
        return msgBean;
    }

}
