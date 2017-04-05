package com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.imp;

import io.netty.handler.logging.LoggingHandler;

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
    public byte[] signatureElement(MessageBean bean) throws HZRealTransferException {
        byte[] signature = null;
        CMT386Bean msgBodyBean = (CMT386Bean) bean.getMessageBean();
        String msgBody = JSONObject.toJSONString(msgBodyBean);
        try {
            signature = RSAUtils.signBytes(msgBody.getBytes("utf-8"), ParamsUtil.getInstance().getPrivateKey());
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

    
    public static void main(String[] args) {
        String pk = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKYb4l9iF+mm1H88zOp81yyuqkn6euCbjUT8C3XKQt0vIza6H+umIeedPmw4J9iXxyS+HxaVDVoOtC+1r//wwypVVnFITkAOuMaKpcKF3Nnr7J83RuP7GaQG6PAEwot14Mbn5kjqFiNwBxcV9ZLoDYmXPhn3W+oATFtHWIwGZtx/AgMBAAECgYBBk8aV43MPUkaYCNfZRvnn8PjXheSW4bIu5tZbZUNqcN6VD3vkm0zNVJ29OZo5fwomrkw1rRh9Ukq+fxsBjvptDDrins8HsKMnJ2C8kiGDsQgR+go87kx4ijwhLENbVJYlGliiEcaQBAQ7/Wv2Kea+zmuqI1uOuS8ygaPNA0j7cQJBAM8ZpM73tr3BW2wnNggdR2chLfOI8lVn6qN3N21+c0D2ctSnKJXjbMeRCFpqCqMs/8To0q17Z1LvsG0rJgqqm+kCQQDNVHzTjzUCN8OrZJOHCdULOTi/CPbWJ0g6Z7k9ctIBgkBcSlAV5Wt74lAjd6Dyfxw6iYQM5nndH5oIgppsWrwnAkEArvMn0ZtbaZNlKsk5EMPDJPDXg6rFpLZjdfHpBftTW6aVvOT1GSIFK9VuLAr2r3/9FRflmZ3s3BgVfzN7MHmNuQJAPCuHDOQJb6XxxBcMGGVsNgbFt02kL+uBAgSFRxmPqvuFasYoI9KAJeSTAWG8G3PeTNUhijo7+e2Z0Oac5CXOvwJBAJHLyaHwWn72evZD9A9KBR3/qSM2KaORyTIm55wl/6yYbOAs/FZp51lieQodWC1jHoYQwO4GpCgDjdJNW4xQdro=";
        String str = "我是原文abc";
        byte[] signatureBytes = null;
        try {
            signatureBytes = RSAUtils.signBytes(str.getBytes("UTF-8"), pk);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String signature = null;
        try {
            signature = new String(signatureBytes, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("原文:" + str);
        System.out.println("签名信息:" + signature);
        System.out.println("签名信息长度byte:" + signatureBytes.length);
        System.out.println("签名信息长度String:" + signature.length());
        
        // System.out.println("签名信息:" + CryptoUtil.bytes2string(signatureBytes,
        // 16));
    }
}
