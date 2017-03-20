package com.zcbspay.platform.hz.realtime.transfer.message.assemble;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.common.bean.MessageHeaderBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageAssemble;
import com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageBean;
import com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.AssembleMsgHeadBase;
import com.zcbspay.platform.hz.realtime.transfer.message.assemble.detail.AssembleSignBase;

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
	public String signature(
			com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageBean bean) {
				return null;
		
	}

	@Override
	public String createMessageHead(
			com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageHeaderBean beanHead) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String assemble(
			com.zcbspay.platform.hz.realtime.transfer.message.api.assemble.MessageHeaderBean beanHead,
			MessageBean beanBody) {
		// TODO Auto-generated method stub
		return null;
	}
}
