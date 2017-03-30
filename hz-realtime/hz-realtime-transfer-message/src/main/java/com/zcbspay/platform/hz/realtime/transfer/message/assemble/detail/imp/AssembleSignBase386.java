package com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.imp;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.common.utils.secret.RSAUtils;
import com.zcbspay.platform.hz.realtime.message.bean.CMT386Bean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.ErrorCodeHZ;
import com.zcbspay.platform.hz.realtime.transfer.message.api.exception.HZRealTransferException;
import com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.AssembleSignBase;
import com.zcbspay.platform.hz.realtime.transfer.message.util.ParamsUtil;

@Service("assembleSignBase386")
public class AssembleSignBase386 implements AssembleSignBase {

    Logger logger = LoggerFactory.getLogger(AssembleSignBase384.class);

    @Override
    public String signatureElement(MessageBean bean) throws HZRealTransferException {
        String signature = null;
        CMT386Bean msgBodyBean = (CMT386Bean) bean.getMessageBean();
        String msgBody = JSONObject.toJSONString(msgBodyBean);
        try {
            signature = RSAUtils.sign(msgBody.getBytes("utf-8"), ParamsUtil.getInstance().getPrivateKey());
        }
        catch (UnsupportedEncodingException e) {
            logger.error("msgbody sign failed:UnsupportedEncodingException", e);
            throw new HZRealTransferException(ErrorCodeHZ.SIGN_FAILED);
        }
        catch (Exception e) {
            logger.error("msgbody sign failed:Exception", e);
            throw new HZRealTransferException(ErrorCodeHZ.SIGN_FAILED);
        }
        return signature;
    }

}
