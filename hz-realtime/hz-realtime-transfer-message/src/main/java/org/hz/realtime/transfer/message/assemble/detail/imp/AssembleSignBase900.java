package org.hz.realtime.transfer.message.assemble.detail.imp;

import java.io.UnsupportedEncodingException;

import org.hz.realtime.common.enums.ErrorCodeHZ;
import org.hz.realtime.common.exception.HZQSZXException;
import org.hz.realtime.common.utils.secret.RSAUtils;
import org.hz.realtime.transfer.message.assemble.detail.AssembleSignBase;
import org.hz.realtime.transfer.message.util.ParamsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.common.MessageBean;

@Service("assembleSignBase900")
public class AssembleSignBase900 implements AssembleSignBase {

    Logger logger = LoggerFactory.getLogger(AssembleSignBase384.class);

    @Override
    public String signatureElement(MessageBean bean) throws HZQSZXException {
        String signature = null;
        CMS900Bean msgBodyBean = (CMS900Bean) bean.getCNAPSMessageBean();
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
