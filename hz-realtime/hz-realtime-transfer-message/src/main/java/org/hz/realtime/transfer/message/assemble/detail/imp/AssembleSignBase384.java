package org.hz.realtime.transfer.message.assemble.detail.imp;

import java.io.UnsupportedEncodingException;

import org.hz.realtime.transfer.message.assemble.detail.AssembleSignBase;
import org.hz.realtime.transfer.message.util.ParamsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.common.enums.ErrorCodeHZ;
import com.zcbspay.platform.hz.realtime.common.exception.HZQSZXException;
import com.zcbspay.platform.hz.realtime.common.utils.secret.RSAUtils;
import com.zcbspay.platform.hz.realtime.message.bean.CMT384Bean;
import com.zcbspay.platform.hz.realtime.message.common.MessageBean;

@Service("assembleSignBase384")
public class AssembleSignBase384 implements AssembleSignBase {

    Logger logger = LoggerFactory.getLogger(AssembleSignBase384.class);

    @Override
    public String signatureElement(MessageBean bean) throws HZQSZXException {
        String signature = null;
        CMT384Bean msgBodyBean = (CMT384Bean) bean.getCNAPSMessageBean();
        String msgBody = JSONObject.toJSONString(msgBodyBean);
        try {
            signature = RSAUtils.sign(msgBody.getBytes("utf-8"), ParamsUtil.getInstance().getPrivateKey());
        }
        catch (UnsupportedEncodingException e) {
            logger.error("msgbody sign failed:UnsupportedEncodingException", e);
            throw new HZQSZXException(ErrorCodeHZ.SIGN_FAILED);
        }
        catch (Exception e) {
            logger.error("msgbody sign failed:Exception", e);
            throw new HZQSZXException(ErrorCodeHZ.SIGN_FAILED);
        }
        return signature;
    }

}
