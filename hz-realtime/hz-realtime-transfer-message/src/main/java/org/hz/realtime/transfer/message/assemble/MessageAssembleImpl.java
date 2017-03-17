package org.hz.realtime.transfer.message.assemble;

import javax.annotation.Resource;

import org.hz.realtime.transfer.message.api.assemble.MessageAssemble;
import org.hz.realtime.transfer.message.api.bean.MessageHeaderBean;
import org.hz.realtime.transfer.message.assemble.detail.AssembleMsgHeadBase;
import org.hz.realtime.transfer.message.assemble.detail.AssembleSignBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.common.exception.HZQSZXException;
import com.zcbspay.platform.hz.realtime.message.bean.CMS316Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMS991Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT384Bean;
import com.zcbspay.platform.hz.realtime.message.bean.CMT386Bean;
import com.zcbspay.platform.hz.realtime.message.common.MessageBean;
import com.zcbspay.platform.hz.realtime.message.common.MessageTypeEnum;

@Service("messageAssemble")
public class MessageAssembleImpl implements MessageAssemble {

    Logger logger = LoggerFactory.getLogger(MessageAssembleImpl.class);

    @Resource
    private AssembleMsgHeadBase assembleMsgHeadBase;
    @Resource
    private AssembleSignBase assembleSignBase384;
    @Resource
    private AssembleSignBase assembleSignBase386;
    @Resource
    private AssembleSignBase assembleSignBase316;
    @Resource
    private AssembleSignBase assembleSignBase900;
    @Resource
    private AssembleSignBase assembleSignBase991;

    @Override
    public String createMessageHead(MessageHeaderBean beanHead) {
        return assembleMsgHeadBase.createMessageHead(beanHead);
    }

    @Override
    public String signature(MessageBean bean) {
        MessageTypeEnum messageType = bean.getBeanType();
        String signature = null;
        try {
            if (messageType == MessageTypeEnum.CMT384) {
                // 实时代收业务报文（CMT384）
                signature = assembleSignBase384.signatureElement(bean);
            }
            else if (messageType == MessageTypeEnum.CMT386) {
                // 实时代付业务报文(CMT386)
                signature = assembleSignBase386.signatureElement(bean);
            }
            else if (messageType == MessageTypeEnum.CMS316) {
                // 业务状态查询报文（CMS316）
                signature = assembleSignBase316.signatureElement(bean);
            }
            else if (messageType == MessageTypeEnum.CMS900) {
                // 通用处理确认报文（CMS900）
                signature = assembleSignBase900.signatureElement(bean);
            }
            else if (messageType == MessageTypeEnum.CMS991) {
                // 通讯探测报文（CMS991）
                signature = assembleSignBase991.signatureElement(bean);
            }
        }
        catch (HZQSZXException e) {
            logger.error(e.getErrCode() + e.getErrMsg());
        }
        return signature;
    }

    @Override
    public String assemble(MessageHeaderBean beanHead, MessageBean bean) {
        // 报文完整信息
        String msgAll = null;
        // 报文头信息
        String msgHeader = null;
        // 数字签名域
        String msgSign = signature(bean);
        // 报文体
        String msgBody = null;
        MessageTypeEnum messageType = bean.getBeanType();
        if (messageType == MessageTypeEnum.CMT384) {
            // 实时代收业务报文（CMT384）
            CMT384Bean msgBodyBean = (CMT384Bean) bean.getCNAPSMessageBean();
            msgBody = JSONObject.toJSONString(msgBodyBean);
        }
        else if (messageType == MessageTypeEnum.CMT386) {
            // 实时代付业务报文(CMT386)
            CMT386Bean msgBodyBean = (CMT386Bean) bean.getCNAPSMessageBean();
            msgBody = JSONObject.toJSONString(msgBodyBean);
        }
        else if (messageType == MessageTypeEnum.CMS316) {
            // 业务状态查询报文（CMS316）
            CMS316Bean msgBodyBean = (CMS316Bean) bean.getCNAPSMessageBean();
            msgBody = JSONObject.toJSONString(msgBodyBean);
        }
        else if (messageType == MessageTypeEnum.CMS900) {
            // 通用处理确认报文（CMS900）
            CMS900Bean msgBodyBean = (CMS900Bean) bean.getCNAPSMessageBean();
            msgBody = JSONObject.toJSONString(msgBodyBean);
        }
        else if (messageType == MessageTypeEnum.CMS991) {
            // 通讯探测报文（CMS991）
            CMS991Bean msgBodyBean = (CMS991Bean) bean.getCNAPSMessageBean();
            msgBody = JSONObject.toJSONString(msgBodyBean);
        }
        beanHead.setMsgBodyLength(Integer.toString(msgBody.length()));
        msgHeader = createMessageHead(beanHead);
        msgAll = msgHeader + msgSign + msgBody;
        return msgAll;
    }
}
