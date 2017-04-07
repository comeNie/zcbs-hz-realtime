package com.zcbspay.platform.hz.realtime.fe.send;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.fe.net.netty.client.NettyClientBootstrap;
import com.zcbspay.platform.hz.realtime.fe.net.netty.client.SocketChannelHelper;
import com.zcbspay.platform.hz.realtime.fe.net.netty.client.sync.NettySyncClient;
import com.zcbspay.platform.hz.realtime.fe.util.ParamsUtil;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.MessageSend;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.bean.MessageBeanStr;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.ErrorCodeFeHZ;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.exception.HZRealFeException;

@Service("messageSend")
public class MessageSendImpl implements MessageSend {

    private static final Logger logger = LoggerFactory.getLogger(MessageSendImpl.class);
    private byte[] sendMsg;

    @Override
    public String sendMessage(MessageBeanStr messageBean) throws HZRealFeException {
        String detectRsp = null;
        MessageTypeEnum msgTypeEnum = messageBean.getMessageType();
        sendMsg = messageBean.getSendMsgBytes();
        logger.info("[sendMsg length is~~~]:" + sendMsg.length);

        if (MessageTypeEnum.CMS991.equals(msgTypeEnum)) {
            // 查询和报文探测netty阻塞同步返回结果
            SocketChannelHelper socketChannelHelper = SocketChannelHelper.getInstance();
            // 主机名称
            String hostAddress = socketChannelHelper.getMessageConfigService().getString("HOST_ADDRESS", ParamsUtil.getInstance().getHzqszx_ip());
            // 主机端口
            int hostPort = socketChannelHelper.getMessageConfigService().getInt("HOST_PORT", Integer.parseInt(ParamsUtil.getInstance().getHzqszx_port()));
            NettySyncClient client = new NettySyncClient();
            try {
                detectRsp = client.sendMessage(hostAddress, hostPort, sendMsg);
            }
            catch (Exception e) {
                logger.error("【send message to HangZhou Clearing Center failed！！！】", e);
                throw new HZRealFeException(ErrorCodeFeHZ.SEND_FAILED);
            }
        }
        else {
            // 实时代收付、业务状态查询NIO接收“同步技术应答”
            int reqPoolSize = 1;
            // 初始化线程池
            ExecutorService executors = Executors.newFixedThreadPool(reqPoolSize);
            for (int i = 0; i < reqPoolSize; i++) {
                executors.execute(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            SocketChannelHelper socketChannelHelper = SocketChannelHelper.getInstance();
                            String hostAddress = socketChannelHelper.getMessageConfigService().getString("HOST_ADDRESS", ParamsUtil.getInstance().getHzqszx_ip());// 主机名称
                            int hostPort = socketChannelHelper.getMessageConfigService().getInt("HOST_PORT", Integer.parseInt(ParamsUtil.getInstance().getHzqszx_port()));// 主机端口
                            NettyClientBootstrap bootstrap = new NettyClientBootstrap(hostPort, hostAddress);
                            bootstrap.sendMessage(sendMsg);
                        }
                        catch (Exception e) {
                            logger.error("【send message to HangZhou Clearing Center failed！！！】", e);
                            // TODO todomxw 更新流水记录失败信息
                        }
                    }
                });
            }
            executors.shutdown();
        }

        return detectRsp;
    }
}
