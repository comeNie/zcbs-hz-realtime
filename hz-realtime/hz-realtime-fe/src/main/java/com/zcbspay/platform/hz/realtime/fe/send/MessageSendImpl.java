package com.zcbspay.platform.hz.realtime.fe.send;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.common.constant.Constant;
import com.zcbspay.platform.hz.realtime.fe.net.netty.NettyClientBootstrap;
import com.zcbspay.platform.hz.realtime.fe.net.netty.SocketChannelHelper;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.MessageSend;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBeanStr;

@Service("messageSendHZ")
public class MessageSendImpl implements MessageSend {

    private static final Logger logger = LoggerFactory.getLogger(MessageSendImpl.class);
    private byte[] sendMsg;

    @Override
    public void sendMessage(MessageBeanStr messageBean) {
        sendMsg = messageBean.getSendMsg().getBytes();
        int reqPoolSize = 1;
        // 初始化线程池
        ExecutorService executors = Executors.newFixedThreadPool(reqPoolSize);
        for (int i = 0; i < reqPoolSize; i++) {
            executors.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        SocketChannelHelper socketChannelHelper = SocketChannelHelper.getInstance();
                        String hostAddress = socketChannelHelper.getMessageConfigService().getString("HOST_ADDRESS", Constant.getInstance().getHzqszx_ip());// 主机名称
                        int hostPort = socketChannelHelper.getMessageConfigService().getInt("HOST_PORT", Integer.parseInt(Constant.getInstance().getHzqszx_port()));// 主机端口
                        NettyClientBootstrap bootstrap = NettyClientBootstrap.getInstance(hostAddress, hostPort);
                        bootstrap.sendMessage(sendMsg);
                    }
                    catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            });
        }
        executors.shutdown();
    }

}
