package com.zcbspay.platform.hz.realtime.transfer.message.unpack;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.common.utils.secret.CryptoUtil;
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
    public MessageRespBean unpack(byte[] msgInfo) throws HZRealTransferException {

        MessageRespBean respBean = null;

        byte[] msgHeader = ArrayUtils.subarray(msgInfo, 0, ParamsUtil.getInstance().getMsgHeaderLength());
        String msgHeaderStr = CryptoUtil.bytes2string(msgHeader, 16);
        logger.info("[msgHeaderStr is ]:" + msgHeaderStr);

        byte[] msgSign = ArrayUtils.subarray(msgInfo, ParamsUtil.getInstance().getMsgHeaderLength(), ParamsUtil.getInstance().getMsgHeaderLength() + ParamsUtil.getInstance().getMsgSignLength());
        String msgSignStr = CryptoUtil.bytes2string(msgSign, 16);
        logger.info("[msgSignStr is ]:" + msgSignStr);

        int subBegin = ParamsUtil.getInstance().getMsgHeaderLength() + ParamsUtil.getInstance().getMsgSignLength();
        int subEnd = msgInfo.length - subBegin;
        byte[] msgBody = ArrayUtils.subarray(msgInfo, subBegin, subEnd);
        String msgBodyStr = CryptoUtil.bytes2string(msgSign, 16);
        logger.info("[msgBodyStr is ]:" + msgBodyStr);

        checkSignature(msgSignStr, msgBody);

        MessageHeaderBean headerBean = getMessageHeaderBean(msgHeaderStr);
        respBean = new MessageRespBean();
        respBean.setMessageHeaderBean(headerBean);
        respBean.setMsgBody(msgBodyStr);

        return respBean;
    }

    /**
     * 校验签名
     * 
     * @param msgSignStr
     * @param msgBodyStr
     * @throws HZRealTransferException
     */
    private void checkSignature(String msgSignStr, byte[] msgBody) throws HZRealTransferException {
        try {
            if (!RSAUtils.verify(msgBody, ParamsUtil.getInstance().getPrivateKey(), msgSignStr)) {
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
     * @param msgHeaderStr
     * @return
     */
    private MessageHeaderBean getMessageHeaderBean(String msgHeaderStr) {
        MessageHeaderBean headerBean = new MessageHeaderBean();
        headerBean.setBusinessType(msgHeaderStr.substring(4, 6));
        headerBean.setSender(msgHeaderStr.substring(10, 10));
        headerBean.setReciever(msgHeaderStr.substring(20, 10));
        headerBean.setComRefId(msgHeaderStr.substring(30, 16));
        headerBean.setSendTime(msgHeaderStr.substring(46, 14));
        logger.info("[MessageHeaderBean is ]:" + headerBean);
        return headerBean;
    }
}
