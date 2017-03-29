package com.zcbspay.platform.hz.realtime.transfer.message.unpack;

import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageRespBean;
import com.zcbspay.platform.hz.realtime.transfer.message.api.unpack.MessageUnpack;

@Service("messageUnpack")
public class MessageUnpackImpl implements MessageUnpack {

    @Override
    public MessageRespBean unpack(byte[] msgInfo) {
        // TODO Auto-generated method stub
        return null;
    }

}
