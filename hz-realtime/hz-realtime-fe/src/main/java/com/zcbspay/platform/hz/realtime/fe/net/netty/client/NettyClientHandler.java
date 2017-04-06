package com.zcbspay.platform.hz.realtime.fe.net.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.ByteArrayInputStream;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.zcbspay.platform.hz.realtime.common.utils.SpringContext;
import com.zcbspay.platform.hz.realtime.fe.net.netty.remote.RemoteAdapter;
import com.zcbspay.platform.hz.realtime.fe.util.ParamsUtil;
import com.zcbspay.platform.hz.realtime.message.bean.fe.service.enums.MessageTypeEnum;
import com.zcbspay.platform.hz.realtime.transfer.message.api.bean.MessageRespBean;

/**
 * Client端接收同步应答
 * 
 * @author AlanMa
 *
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    RemoteAdapter remoteAdapterHZ = (RemoteAdapter) SpringContext.getContext().getBean("remoteAdapterHZ");

    /**
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        logger.info("enter NettyClientHandler.channelRead0 ~~~");
        SocketChannelHelper socketChannelHelper = SocketChannelHelper.getInstance();
        String hostName = socketChannelHelper.getMessageConfigService().getString("HOST_NAME");// 主机名称
        String hostAddress = socketChannelHelper.getMessageConfigService().getString("HOST_ADDRESS");// 主机名称
        int hostPort = socketChannelHelper.getMessageConfigService().getInt("HOST_PORT", Integer.parseInt(ParamsUtil.getInstance().getHzqszx_port()));// 主机端口
        String charset = socketChannelHelper.getMessageConfigService().getString("CHARSET");// 字符集
        int headLength = socketChannelHelper.getMessageConfigService().getInt("HEAD_LENGTH", 61);// 报文头长度位数
        int signLength = socketChannelHelper.getMessageConfigService().getInt("SIGN_LENGTH", 128);// 数字签名域长度
        int maxSingleLength = socketChannelHelper.getMessageConfigService().getInt("MAX_SINGLE_LENGTH", 200 * 1024);// 单个报文最大长度，单位：字节
        int msgAllLengthIndex = 4;
        String body = new String(msg, charset);
        logger.info("SERVER接收到消息:{}", body);
        logger.info("SERVER接收到消息长度:{}", msg.length);
        ByteArrayInputStream input = new ByteArrayInputStream(msg);
        SocketChannelHelper socketHelper = SocketChannelHelper.getInstance();

        /**
         * 1、读取报文头
         */
        byte[] bytes = socketHelper.getReceivedBytes();
        if (bytes == null) {
            bytes = new byte[0];
        }
        if (bytes.length < headLength) {
            byte[] headBytes = new byte[headLength - bytes.length];
            int couter = input.read(headBytes);
            if (couter < 0) {
                logger.error("连接[{} --> {}-{}:{}]已关闭", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort });
                return;
            }
            bytes = ArrayUtils.addAll(bytes, ArrayUtils.subarray(headBytes, 0, couter));
            if (couter < headBytes.length) {// 未满足长度位数，可能是粘包造成，保存读取到的
                socketHelper.setReceivedBytes(bytes);
                return;
            }
        }
        String headAllLength = new String(ArrayUtils.subarray(bytes, 0, msgAllLengthIndex), charset);
        int bodyLength = NumberUtils.toInt(headAllLength) - (headLength - msgAllLengthIndex) - signLength;
        if (bodyLength <= 0 || bodyLength > maxSingleLength * 1024) {
            logger.error("连接[{} --> {}-{}:{}]出现脏数据，自动断链：{}", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort, new String(bytes, charset) });
            return;
        }
        byte[] headBytes = ArrayUtils.subarray(bytes, 0, headLength);
        logger.info("本地[{}] <-- 对端[{}-{}:{}] ## {}", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort, new String(headBytes, charset) });

        /**
         * 2、读取数字签名域
         */
        if (bytes.length < headLength + signLength) {
            // 未读取的数字签名域长度
            byte[] signBytes = new byte[headLength + signLength - bytes.length];
            int couter = input.read(signBytes);
            if (couter < 0) {
                logger.error("连接[{} --> {}-{}:{}]已关闭", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort });
                return;
            }
            bytes = ArrayUtils.addAll(bytes, ArrayUtils.subarray(signBytes, 0, couter));
            if (couter < signBytes.length) {
                // 未满足长度位数，可能是粘包造成，保存读取到的
                socketHelper.setReceivedBytes(bytes);
                return;
            }
        }
        byte[] signBytes = ArrayUtils.subarray(bytes, headLength, headLength + signLength);
        logger.info("本地[{}] <-- 对端[{}-{}:{}] ## {}", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort, new String(signBytes, charset) });

        /**
         * 3、读取报文体
         */
        // 是否需要继续读取报文体数据
        logger.info("[headAllLength is]:" + headAllLength);
        logger.info("[bytes.length is]:" + bytes.length);
        if (bytes.length < NumberUtils.toInt(headAllLength) + msgAllLengthIndex) {
            // 未读取的报文体长度
            logger.info("[headLength is]:" + headLength);
            logger.info("[signLength is]:" + signLength);
            logger.info("[bodyLength is]:" + bodyLength);
            logger.info("[bytes.length is]:" + bytes.length);
            byte[] bodyBytes = new byte[headLength + signLength + bodyLength - bytes.length];
            logger.info("[bodyBytes length is]:" + bodyBytes.length);
            int couter = input.read(bodyBytes);
            logger.info("[couter is]:" + couter);
            if (couter < 0) {
                logger.error("连接[{} --> {}-{}:{}]已关闭", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort });
                return;
            }
            bytes = ArrayUtils.addAll(bytes, ArrayUtils.subarray(bodyBytes, 0, couter));
            logger.info("[~~~bytes length is]:" + bytes.length);
            if (couter < bodyBytes.length) {
                // 未满足长度位数，可能是粘包造成，保存读取到的
                socketHelper.setReceivedBytes(bytes);
                return;
            }
        }
        byte[] bodyBytes = ArrayUtils.subarray(bytes, headLength + signLength, headLength + signLength + bodyLength);
        logger.info("本地[{}] <-- 对端[{}-{}:{}] ## {}", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort, new String(bodyBytes, charset) });

        // 解析报文
        MessageRespBean messageRespBean = null;
        try {
            messageRespBean = remoteAdapterHZ.unpack(headBytes, signBytes, bodyBytes);
        }
        catch (Exception e) {
            // TODO mxwtodo 记录异常信息至报文流水表
            logger.error("message unpack is failed!!!", e);

        }

        String businessType = messageRespBean.getMessageHeaderBean().getBusinessType();
        com.zcbspay.platform.hz.realtime.business.message.service.bean.MessageRespBean respbean = new com.zcbspay.platform.hz.realtime.business.message.service.bean.MessageRespBean();
        BeanUtils.copyProperties(messageRespBean, respbean);

        if (MessageTypeEnum.CMS900.value().equals(businessType)) {
            // 通用处理确认报文（CMS900）
            remoteAdapterHZ.commProcAfrmResp(respbean);
        }
        else if (MessageTypeEnum.CMS911.value().equals(businessType)) {
            // 报文丢弃通知报文（CMS911）
            remoteAdapterHZ.discardMessage(respbean);
        }
        else if (MessageTypeEnum.CMS992.value().equals(businessType)) {
            // 探测回应报文（CMS992）
            remoteAdapterHZ.detectResponse(respbean);
        }
        else if (MessageTypeEnum.CMS317.value().equals(businessType)) {
            // 业务状态查询应答报文（CMS317）
            remoteAdapterHZ.busStaQryResp(respbean);
        }
    }
}
