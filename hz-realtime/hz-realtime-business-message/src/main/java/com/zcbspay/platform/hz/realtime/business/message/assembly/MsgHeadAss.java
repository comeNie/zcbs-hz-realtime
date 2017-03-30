package com.zcbspay.platform.hz.realtime.business.message.assembly;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.business.message.enums.OrgCode;
import com.zcbspay.platform.hz.realtime.business.message.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateStyle;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateTimeUtils;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.MessageTypeEnum;

@Service
public class MsgHeadAss implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MsgHeadAss.class);

    private static SerialNumberService redisSerialNumberService;

    @Resource(name = "redisSerialNumberService")
    private SerialNumberService redisSerialNumberServiceBean;

    public static MessageHeaderBean commMsgHeaderReq() {
        MessageHeaderBean header = new MessageHeaderBean();
        header.setBusinessType(MessageTypeEnum.CMS316.value());
        header.setSender(OrgCode.ZCBS.getValue());
        header.setReciever(OrgCode.HZQSZX.getValue());
        header.setSendTime(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        header.setComRefId(redisSerialNumberService.generateHZComuRefId());
        return header;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("[enter afterPropertiesSet~~~]");
        MsgHeadAss.redisSerialNumberService = redisSerialNumberServiceBean;
    }

}
