package com.zcbspay.platform.hz.realtime.application.consumer.listener;

import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.base.Charsets;
import com.zcbspay.platform.hz.realtime.application.enums.HZRealTimeEnum;
import com.zcbspay.platform.hz.realtime.application.service.ConcentrateCacheResultService;
import com.zcbspay.platform.hz.realtime.application.service.ConcentrateTradeService;
import com.zcbspay.platform.hz.realtime.application.service.bean.TradeBean;
import com.zcbspay.platform.hz.realtime.business.message.service.bean.ResultBean;

@Service("hzRealTimeListener")
public class HZRealTimeListener implements MessageListenerConcurrently{
	private static final Logger log = LoggerFactory.getLogger(HZRealTimeListener.class);
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("producer_hz_realtime");
	private static final String KEY = "HZREALTIME:";
	
	@Autowired
	private ConcentrateTradeService concentrateTradeService;
	@Autowired
	private ConcentrateCacheResultService concentrateCacheResultService;
	
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
			ConsumeConcurrentlyContext arg1) {
		String json = null;
		for (MessageExt msg : msgs) {
			ResultBean resultBean = null;
			if (msg.getTopic().equals(RESOURCE.getString("hz.realtime.subscrib"))) {
				HZRealTimeEnum hzRealTimeEnum = HZRealTimeEnum.fromValue(msg.getTags());
				if(hzRealTimeEnum == HZRealTimeEnum.REALTIME_COLLECT){//实时代收
					json = new String(msg.getBody(), Charsets.UTF_8);
					log.info("接收到的MSG:" + json);
					log.info("接收到的MSGID:" + msg.getMsgId());
					TradeBean tradeBean = JSON.parseObject(json,TradeBean.class);
					if (tradeBean == null) {
						log.warn("MSGID:{}JSON转换后为NULL,无法生成订单数据,原始消息数据为{}",msg.getMsgId(), json);
						break;
					}
					resultBean = concentrateTradeService.realTimeCollection(tradeBean);
					
				}else if(hzRealTimeEnum == HZRealTimeEnum.REALTIME_PAYMENT){//实时代付
					json = new String(msg.getBody(), Charsets.UTF_8);
					log.info("接收到的MSG:" + json);
					log.info("接收到的MSGID:" + msg.getMsgId());
					TradeBean tradeBean = JSON.parseObject(json,TradeBean.class);
					if (tradeBean == null) {
						log.warn("MSGID:{}JSON转换后为NULL,无法生成订单数据,原始消息数据为{}",msg.getMsgId(), json);
						break;
					}
					resultBean = concentrateTradeService.realTimePayment(tradeBean);
					
				}else if(hzRealTimeEnum == HZRealTimeEnum.TRADE_QUERY){
					json = new String(msg.getBody(), Charsets.UTF_8);
					log.info("接收到的MSG:" + json);
					log.info("接收到的MSGID:" + msg.getMsgId());
					TradeBean tradeBean = JSON.parseObject(json,TradeBean.class);
					if (tradeBean == null) {
						log.warn("MSGID:{}JSON转换后为NULL,无法生成订单数据,原始消息数据为{}",msg.getMsgId(), json);
						break;
					}
					resultBean = concentrateTradeService.queryTrade(tradeBean.getTxnseqno());
				}else if(hzRealTimeEnum == HZRealTimeEnum.LINK_CHECK){
					json = new String(msg.getBody(), Charsets.UTF_8);
					log.info("接收到的MSG:" + json);
					log.info("接收到的MSGID:" + msg.getMsgId());
					TradeBean tradeBean = JSON.parseObject(json,TradeBean.class);
					if (tradeBean == null) {
						log.warn("MSGID:{}JSON转换后为NULL,无法生成订单数据,原始消息数据为{}",msg.getMsgId(), json);
						break;
					}
					resultBean = concentrateTradeService.checkLink();
				}
			}
			concentrateCacheResultService.saveInsteadPayResult(KEY, JSON.toJSONString(resultBean));
			log.info(Thread.currentThread().getName()+ " Receive New Messages: " + msgs);
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
