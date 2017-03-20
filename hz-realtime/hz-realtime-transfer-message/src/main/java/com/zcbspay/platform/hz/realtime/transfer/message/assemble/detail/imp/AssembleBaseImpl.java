package com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageHeaderBean;
import com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.AssembleMsgHeadBase;

@Service("assembleMsgHeadBase")
public class AssembleBaseImpl implements AssembleMsgHeadBase {

    @Resource
    private SerialNumberService redisSerialNumberService;

    @Override
    public String createMessageHead(MessageHeaderBean beanHead) {
        // 报文头
        StringBuffer msgHeader = new StringBuffer();
        // 报文长度 0 4
        String msgLength = beanHead.getMsgBodyLength();
        // 业务类型 4 6
        String optType = beanHead.getBusinessType();
        // 报文发起人 10 10
        String sender = beanHead.getSender();
        // 报文接收人 20 10
        String receiver = beanHead.getReciever();
        // 通信参考号 30 16
        String msgRefId = redisSerialNumberService.generateHZComuRefId();
        // 报文发送时间 46 14
        String sendTime = beanHead.getSendTime();
        // 报文体格式 60 1
        String formate = "J";

        msgHeader.append(msgLength);
        msgHeader.append(optType);
        msgHeader.append(sender);
        msgHeader.append(receiver);
        msgHeader.append(msgRefId);
        msgHeader.append(sendTime);
        msgHeader.append(formate);

        return msgHeader.toString();
    }

}
