package com.zcbspay.platform.hz.realtime.transfer.message.assemble;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.message.bean.CMS316Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS991Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT384Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT386Bean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageAssemble;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.ErrorCodeTransHZ;
import com.zcbspay.platform.hz.realtime.transfer.message.api.enums.MessageTypeEnum;
import com.zcbspay.platform.hz.realtime.transfer.message.api.exception.HZRealTransferException;
import com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.AssembleMsgHeadBase;
import com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.AssembleSignBase;
import com.zcbspay.platform.hz.realtime.transfer.message.dao.ConfigInfoDao;
import com.zcbspay.platform.hz.realtime.transfer.message.enums.SKKEY;
import com.zcbspay.platform.hz.realtime.transfer.message.pojo.ConfigInfoDO;

@Service("messageAssemble")
public class MessageAssembleImpl implements MessageAssemble {

    Logger logger = LoggerFactory.getLogger(MessageAssembleImpl.class);
    private String charset = "UTF-8";
    @Resource
    private AssembleMsgHeadBase assembleMsgHeadBase;
    @Resource(name = "assembleSignBase384")
    private AssembleSignBase assembleSignBase384;
    @Resource(name = "assembleSignBase386")
    private AssembleSignBase assembleSignBase386;
    @Resource(name = "assembleSignBase316")
    private AssembleSignBase assembleSignBase316;
    @Resource(name = "assembleSignBase991")
    private AssembleSignBase assembleSignBase991;
    @Autowired
    private ConfigInfoDao configInfoDao;
    
    @Override
    public String createMessageHead(MessageHeaderBean beanHead) {
        return assembleMsgHeadBase.createMessageHead(beanHead);
    }

    @Override
    public byte[] signature(MessageBean bean) throws HZRealTransferException {
        MessageTypeEnum messageType = bean.getMessageTypeEnum();
        byte[] signature = null;
        ConfigInfoDO configInfoDO = configInfoDao.getparamByName(SKKEY.HZCC_SHPT_PRIKEY.getValue());
        if (messageType == MessageTypeEnum.CMT384) {
            // 实时代收业务报文（CMT384）
            signature = assembleSignBase384.signatureElement(bean,configInfoDO.getPara());
        }
        else if (messageType == MessageTypeEnum.CMT386) {
            // 实时代付业务报文(CMT386)
            signature = assembleSignBase386.signatureElement(bean,configInfoDO.getPara());
        }
        else if (messageType == MessageTypeEnum.CMS316) {
            // 业务状态查询报文（CMS316）
            signature = assembleSignBase316.signatureElement(bean,configInfoDO.getPara());
        }
        else if (messageType == MessageTypeEnum.CMS991) {
            // 通讯探测报文（CMS991）
            signature = assembleSignBase991.signatureElement(bean,configInfoDO.getPara());
        }
        return signature;
    }

    @Override
    public byte[] assemble(MessageHeaderBean beanHead, MessageBean bean) throws HZRealTransferException {
        // 报文头信息
        String msgHeader = null;
        // 数字签名域
        byte[] msgSign = signature(bean);
        // 报文体
        String msgBody = null;
        MessageTypeEnum messageType = bean.getMessageTypeEnum();
        if (messageType == MessageTypeEnum.CMT384) {
            // 实时代收业务报文（CMT384）
            CMT384Bean msgBodyBean = (CMT384Bean) bean.getMessageBean();
            msgBody = JSONObject.toJSONString(msgBodyBean);
        }
        else if (messageType == MessageTypeEnum.CMT386) {
            // 实时代付业务报文(CMT386)
            CMT386Bean msgBodyBean = (CMT386Bean) bean.getMessageBean();
            msgBody = JSONObject.toJSONString(msgBodyBean);
        }
        else if (messageType == MessageTypeEnum.CMS316) {
            // 业务状态查询报文（CMS316）
            CMS316Bean msgBodyBean = (CMS316Bean) bean.getMessageBean();
            msgBody = JSONObject.toJSONString(msgBodyBean);
        }
        else if (messageType == MessageTypeEnum.CMS900) {
            // 通用处理确认报文（CMS900）
            CMS900Bean msgBodyBean = (CMS900Bean) bean.getMessageBean();
            msgBody = JSONObject.toJSONString(msgBodyBean);
        }
        else if (messageType == MessageTypeEnum.CMS991) {
            // 通讯探测报文（CMS991）
            CMS991Bean msgBodyBean = (CMS991Bean) bean.getMessageBean();
            msgBody = JSONObject.toJSONString(msgBodyBean);
        }
        byte[] msgAllBytes = null;
        try {
            beanHead.setMsgBodyLength(Integer.toString(msgBody.getBytes(charset).length));
            msgHeader = createMessageHead(beanHead);
            msgAllBytes = ArrayUtils.addAll(msgAllBytes,msgHeader.getBytes(charset));
            msgAllBytes = ArrayUtils.addAll(msgAllBytes, msgSign);
            msgAllBytes = ArrayUtils.addAll(msgAllBytes, msgBody.getBytes(charset));
        }
        catch (UnsupportedEncodingException e) {
            logger.error("parse String to byte failed", e);
            throw new HZRealTransferException(ErrorCodeTransHZ.BYTE_PARSE_FAIL);
        }

        return msgAllBytes;
    }
}
