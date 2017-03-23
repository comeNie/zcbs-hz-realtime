package org.hz.realtime.business.message.assembly;

import javax.annotation.Resource;

import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.message.bean.CMS991Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CheckInformationBean;
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
public class ComuDetecAss {

    @Resource
    private static SerialNumberService redisSerialNumberService;

    public static MessageBean communicateDetecMsgBodyReq() {
        MessageBean msgBean = new MessageBean();
        msgBean.setMessageTypeEnum(MessageTypeEnum.CMS991);
        CMS991Bean bean = new CMS991Bean();
        bean.setMsgId(redisSerialNumberService.generateHZMsgId());
        CheckInformationBean checkInfo = new CheckInformationBean();
        checkInfo.setCheckFlag("1");
        bean.setCheckInformationBean(checkInfo);
        msgBean.setMessageBean(bean);
        return msgBean;
    }

}
