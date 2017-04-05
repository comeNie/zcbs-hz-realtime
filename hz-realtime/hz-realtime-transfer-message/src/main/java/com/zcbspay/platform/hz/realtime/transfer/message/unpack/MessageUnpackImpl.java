package com.zcbspay.platform.hz.realtime.transfer.message.unpack;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.common.utils.secret.RSAUtils;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageRespBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.ErrorCodeHZ;
import com.zcbspay.platform.hz.realtime.transfer.message.api.exception.HZRealTransferException;
import com.zcbspay.platform.hz.realtime.transfer.message.api.unpack.MessageUnpack;
import com.zcbspay.platform.hz.realtime.transfer.message.util.ParamsUtil;

@Service("messageUnpack")
public class MessageUnpackImpl implements MessageUnpack {

    private static final Logger logger = LoggerFactory.getLogger(MessageUnpackImpl.class);

    @Override
    public MessageRespBean unpack(byte[] headInfo, byte[] signInfo, byte[] bodyInfo) throws HZRealTransferException {
        String charset = "UTF-8";
        MessageRespBean respBean = null;
        String msgHeaderStr = null;
        String msgSignStr = null;
        String msgBodyStr = null;
        try {
            msgHeaderStr = new String(headInfo, charset);
            logger.info("[msgHeaderStr is ]:" + msgHeaderStr);

            msgSignStr = new String(signInfo, charset);
            logger.info("[msgSignStr is ]:" + msgSignStr);

            msgBodyStr = new String(bodyInfo, charset);
            logger.info("[msgBodyStr is ]:" + msgBodyStr);

            checkSignature(signInfo, bodyInfo);
            logger.info("[pass sign verification~~~]");

            MessageHeaderBean headerBean = getMessageHeaderBean(msgHeaderStr);
            respBean = new MessageRespBean();
            respBean.setMessageHeaderBean(headerBean);
            respBean.setMsgBody(msgBodyStr);
        }
        catch (UnsupportedEncodingException e) {
            logger.error("byte to string exception~~~", e);
        }
        logger.info("[msgBodyStr is ]:" + msgBodyStr);

        return respBean;
    }

    /**
     * 校验签名
     * 
     * @param msgSignStr
     * @param msgBodyStr
     * @throws HZRealTransferException
     */
    private void checkSignature(byte[] msgSign, byte[] msgBody) throws HZRealTransferException {
        try {
            if (!RSAUtils.verifyBytes(msgBody, ParamsUtil.getInstance().getPublicKeyHZQSZX(), msgSign)) {
                logger.error("【response message check sign failed】");
                throw new HZRealTransferException(ErrorCodeHZ.CHECK_SIGN_FAIL);
            }
        }
        catch (Exception e) {
            logger.error("response message check sign failed:Exception", e);
            throw new HZRealTransferException(ErrorCodeHZ.CHECK_SIGN_FAIL);
        }
    }

    /**
     * 获取报文头对象
     * 
     * @param msgHeaderStr
     * @return
     */
    private MessageHeaderBean getMessageHeaderBean(String msgHeaderStr) {
        MessageHeaderBean headerBean = new MessageHeaderBean();
        headerBean.setBusinessType(msgHeaderStr.substring(4, 10));
        headerBean.setSender(msgHeaderStr.substring(10, 20));
        headerBean.setReciever(msgHeaderStr.substring(20, 30));
        headerBean.setComRefId(msgHeaderStr.substring(30, 46));
        headerBean.setSendTime(msgHeaderStr.substring(46, 60));
        logger.info("[MessageHeaderBean is ]:" + headerBean);
        return headerBean;
    }
}
