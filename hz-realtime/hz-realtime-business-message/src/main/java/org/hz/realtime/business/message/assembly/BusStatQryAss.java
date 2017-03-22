package org.hz.realtime.business.message.assembly;

import java.util.Date;

import javax.annotation.Resource;

import org.hz.realtime.business.message.enums.OrgCode;

import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateStyle;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateTimeUtils;
import com.zcbspay.platform.hz.realtime.message.bean.CMS316Bean;
import com.zcbspay.platform.hz.realtime.message.bean.OrgnlTxBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;
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

    public static MessageHeaderBean busStatusQryMsgHeaderReq() {
        MessageHeaderBean header = new MessageHeaderBean();
        header.setBusinessType(MessageTypeEnum.CMS316.value());
        // TODO 机构号是否与清算行号一致？
        header.setSender(OrgCode.ZCBS.getValue());
        header.setReciever(OrgCode.HZQSZX.getValue());
        header.setSendTime(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        return header;
    }

}
