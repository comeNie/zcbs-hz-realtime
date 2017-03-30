package com.zcbspay.platform.hz.realtime.business.message.assembly;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.business.message.sequence.SerialNumberService;
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
@Service
public class BusStatQryAss implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(BusStatQryAss.class);

    @Resource(name = "redisSerialNumberService")
    private SerialNumberService redisSerialNumberServiceBean;

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

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("[enter afterPropertiesSet~~~]");
        BusStatQryAss.redisSerialNumberService = redisSerialNumberServiceBean;
    }

}
