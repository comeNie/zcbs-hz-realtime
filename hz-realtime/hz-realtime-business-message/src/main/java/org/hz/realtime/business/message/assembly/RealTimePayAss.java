package org.hz.realtime.business.message.assembly;

import java.util.Date;

import org.hz.realtime.business.message.enums.OrgCode;

import com.zcbspay.platform.hz.realtime.business.message.service.bean.SingleCollectionChargesBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.SinglePaymentBean;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateStyle;
import com.zcbspay.platform.hz.realtime.common.utils.date.DateTimeUtils;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.MessageTypeEnum;

/**
 * 实时代付报文拼装
 *
 * @author alanma
 * @version
 * @date 2017年3月6日 上午9:52:07
 * @since
 */
public class RealTimePayAss {

    public static MessageBean realtimeCollMsgBodyReq(SinglePaymentBean paymentBean) {
        MessageBean msgBean = new MessageBean();
        msgBean.setMessageTypeEnum(MessageTypeEnum.CMT386);
        msgBean.setMessageBean(paymentBean);
        return msgBean;
    }

    public static MessageHeaderBean realtimeCollMsgHeaderReq(SinglePaymentBean paymentBean) {
        MessageHeaderBean header = new MessageHeaderBean();
        header.setBusinessType(MessageTypeEnum.CMT386.value());
        // TODO 机构号是否与清算行号一致？
        header.setSender(OrgCode.ZCBS.getValue());
        header.setReciever(OrgCode.HZQSZX.getValue());
        header.setSendTime(DateTimeUtils.formatDateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
        return header;
    }

}
