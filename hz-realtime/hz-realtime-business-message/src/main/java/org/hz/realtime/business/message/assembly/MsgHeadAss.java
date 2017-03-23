package org.hz.realtime.business.message.assembly;

import java.util.Date;

import javax.annotation.Resource;

import org.hz.realtime.business.message.enums.OrgCode;

import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateStyle;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateTimeUtils;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.MessageTypeEnum;

public class MsgHeadAss {

    @Resource
    private static SerialNumberService redisSerialNumberService;

    public static MessageHeaderBean commMsgHeaderReq() {
        MessageHeaderBean header = new MessageHeaderBean();
        header.setBusinessType(MessageTypeEnum.CMS316.value());
        // TODO 机构号是否与清算行号一致？
        header.setSender(OrgCode.ZCBS.getValue());
        header.setReciever(OrgCode.HZQSZX.getValue());
        header.setSendTime(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        header.setComRefId(redisSerialNumberService.generateHZComuRefId());
        return header;
    }

}
