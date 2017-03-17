package com.zcbspay.platform.hz.realtime.fe.net.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.ByteArrayInputStream;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcbspay.platform.hz.realtime.business.message.service.BusinessMessageReceiver;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;
import com.zcbspay.platform.hz.realtime.common.bean.MessageRespBean;
import com.zcbspay.platform.hz.realtime.common.constant.Constant;
import com.zcbspay.platform.hz.realtime.common.enums.MessageTypeEnum;
import com.zcbspay.platform.hz.realtime.common.utils.secret.CryptoUtil;
import com.zcbspay.platform.hz.realtime.transfer.message.api.unpack.MessageUnpack;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月2日 上午8:54:46
 * @since
 */

public class NettyClientHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Resource
    private MessageUnpack messageUnpack;
    @Resource
    private BusinessMessageReceiver businessMessageReceiver;

    /**
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        SocketChannelHelper socketChannelHelper = SocketChannelHelper.getInstance();
        String hostName = socketChannelHelper.getMessageConfigService().getString("HOST_NAME");// 主机名称
        String hostAddress = socketChannelHelper.getMessageConfigService().getString("HOST_ADDRESS");// 主机名称
        int hostPort = socketChannelHelper.getMessageConfigService().getInt("HOST_PORT", Integer.parseInt(Constant.getInstance().getHzqszx_port()));// 主机端口
        String charset = socketChannelHelper.getMessageConfigService().getString("CHARSET");// 字符集
        int headLength = socketChannelHelper.getMessageConfigService().getInt("HEAD_LENGTH", 6);// 报文头长度位数
        int maxSingleLength = socketChannelHelper.getMessageConfigService().getInt("MAX_SINGLE_LENGTH", 200 * 1024);// 单个报文最大长度，单位：字节
        ByteArrayInputStream input = new ByteArrayInputStream(msg);
        SocketChannelHelper socketHelper = SocketChannelHelper.getInstance();
        // inputStream.read(b)
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
        String headMsg = new String(ArrayUtils.subarray(bytes, 0, headLength), charset);
        int bodyLength = NumberUtils.toInt(headMsg);
        if (bodyLength <= 0 || bodyLength > maxSingleLength * 1024) {
            logger.error("连接[{} --> {}-{}:{}]出现脏数据，自动断链：{}", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort, headMsg });
            return;
        }
        /**
         * 2、读取报文体
         */
        if (bytes.length < headLength + bodyLength) {
            byte[] bodyBytes = new byte[headLength + bodyLength - bytes.length];
            int couter = input.read(bodyBytes);
            if (couter < 0) {
                logger.error("连接[{} --> {}-{}:{}]已关闭", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort });
                return;
            }
            bytes = ArrayUtils.addAll(bytes, ArrayUtils.subarray(bodyBytes, 0, couter));
            if (couter < bodyBytes.length) {// 未满足长度位数，可能是粘包造成，保存读取到的
                socketHelper.setReceivedBytes(bytes);
                return;
            }
        }
        byte[] bodyBytes = ArrayUtils.subarray(bytes, headLength, headLength + bodyLength);
        logger.info("本地[{}] <-- 对端[{}-{}:{}] ## {}", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort, CryptoUtil.bytes2string(bodyBytes, 16) });

        // 解析报文
        MessageRespBean messageRespBean = messageUnpack.unpack(bytes);
        String businessType = messageRespBean.getMessageHeaderBean().getBusinessType();
        ResultBean resultBean = null;
        if (MessageTypeEnum.CMT385.value().equals(businessType)) {
            // 实时代收业务回执报文（CMT385）
            resultBean = businessMessageReceiver.realTimeCollectionChargesReceipt(messageRespBean);
        }
        else if (MessageTypeEnum.CMT387.value().equals(businessType)) {
            // 实时代收业务回执报文（CMT387）
            resultBean = businessMessageReceiver.realTimePaymentReceipt(messageRespBean);
        }
        else if (MessageTypeEnum.CMS317.value().equals(businessType)) {
            // 业务状态查询应答报文（CMS317）
            resultBean = businessMessageReceiver.busStaQryResp(messageRespBean);
        }
        else if (MessageTypeEnum.CMS900.value().equals(businessType)) {
            // 通用处理确认报文（CMS900）
            resultBean = businessMessageReceiver.commProcAfrmResp(messageRespBean);
        }
        else if (MessageTypeEnum.CMS911.value().equals(businessType)) {
            // 报文丢弃通知报文（CMS911）
            resultBean = businessMessageReceiver.discardMessage(messageRespBean);
        }
        else if (MessageTypeEnum.CMS992.value().equals(businessType)) {
            // 探测回应报文（CMS992）
            resultBean = businessMessageReceiver.detectResponse(messageRespBean);
        }
        byte[] clearBytes = (byte[]) resultBean.getResultObj();
        socketHelper.setReceivedBytes(clearBytes);
    }
}
