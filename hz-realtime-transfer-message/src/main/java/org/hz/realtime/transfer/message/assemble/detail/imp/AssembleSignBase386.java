package org.hz.realtime.transfer.message.assemble.detail.imp;

import java.io.UnsupportedEncodingException;

import org.hz.realtime.common.sequence.utils.secret.RSAUtils;
import org.hz.realtime.transfer.message.assemble.detail.AssembleSignBase;
import org.hz.realtime.transfer.message.util.ParamsUtil;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.message.bean.CMT386Bean;
import com.zcbspay.platform.hz.realtime.message.common.MessageBean;

@Service("assembleSignBase386")
public class AssembleSignBase386 implements AssembleSignBase {

    @Override
    public String signatureElement(MessageBean bean) {
        String signature = null;
        CMT386Bean msgBodyBean = (CMT386Bean) bean.getCNAPSMessageBean();
        String msgBody = JSONObject.toJSONString(msgBodyBean);
        try {
            signature = RSAUtils.sign(msgBody.getBytes("utf-8"), ParamsUtil.getInstance().getPrivateKey());
        }
        catch (UnsupportedEncodingException e) {
            // TODO mxwtodo
            e.printStackTrace();
        }
        catch (Exception e) {
            // TODO mxwtodo
            e.printStackTrace();
        }
        return signature;
    }

}
