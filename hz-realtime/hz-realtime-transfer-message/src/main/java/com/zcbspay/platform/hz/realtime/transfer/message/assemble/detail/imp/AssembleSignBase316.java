package com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.imp;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.common.enums.ErrorCodeHZ;
import com.zcbspay.platform.hz.realtime.common.exception.HZQSZXException;
import com.zcbspay.platform.hz.realtime.common.utils.secret.RSAUtils;
import com.zcbspay.platform.hz.realtime.message.bean.CMS316Bean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.AssembleSignBase;
import com.zcbspay.platform.hz.realtime.transfer.message.util.ParamsUtil;

@Service("assembleSignBase316")
public class AssembleSignBase316 implements AssembleSignBase {

    Logger logger = LoggerFactory.getLogger(AssembleSignBase316.class);

    @Override
    public String signatureElement(MessageBean bean) throws HZQSZXException {
        String signature = null;
        CMS316Bean msgBodyBean = (CMS316Bean) bean.getMessageBean();
        String msgBody = JSONObject.toJSONString(msgBodyBean);
        try {
            signature = RSAUtils.sign(msgBody.getBytes("utf-8"), ParamsUtil.getInstance().getPrivateKey());
        }
        catch (UnsupportedEncodingException e) {
            logger.error("msgbody sign failed:UnsupportedEncodingException",e);
            throw new HZQSZXException(ErrorCodeHZ.SIGN_FAILED);
        }
        catch (Exception e) {
            logger.error("msgbody sign failed:Exception",e);
            throw new HZQSZXException(ErrorCodeHZ.SIGN_FAILED);
        }
        return signature;
    }

}
