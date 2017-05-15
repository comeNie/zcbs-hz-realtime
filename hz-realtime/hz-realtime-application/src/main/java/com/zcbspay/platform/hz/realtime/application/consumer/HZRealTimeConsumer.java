package com.zcbspay.platform.hz.realtime.application.consumer;

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.zcbspay.platform.hz.realtime.application.enums.HZRealTimeEnum;

@Service
public class HZRealTimeConsumer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(HZRealTimeConsumer.class);
    private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("producer_hz_realtime");
    @Autowired
    @Qualifier("hzRealTimeListener")
    private MessageListenerConcurrently hzRealTimeListener;

    public void startConsume() throws InterruptedException, MQClientException {
        /**
         * 当前例子是PushConsumer用法，使用方式给用户感觉是消息从RocketMQ服务器推到了应用客户端。<br>
         * 但是实际PushConsumer内部是使用长轮询Pull方式从RocketMQ服务器拉消息，然后再回调用户Listener方法<br>
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RESOURCE.getString("hz.realtime.producer.group"));
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr(RESOURCE.getString("single.namesrv.addr"));
        consumer.setInstanceName(RESOURCE.getString("hz.realtime.instancename"));
        String subExpression = "";
        for (HZRealTimeEnum tagsEnum : HZRealTimeEnum.values()) {
            if (StringUtils.isNotEmpty(subExpression)) {
                subExpression += " || ";
            }
            subExpression += tagsEnum.getCode();
        }
        log.info("subExpression:{}", subExpression);
        consumer.subscribe(RESOURCE.getString("hz.realtime.subscribe"), subExpression);
        consumer.registerMessageListener(hzRealTimeListener);// 在监听器中实现创建order
        log.info("NamesrvAddr:{},InstanceName:{},subscribe:{},MessageListener:{}", consumer.getNamesrvAddr(), consumer.getInstanceName(), consumer.getSubscription(), consumer.getMessageListener());
        consumer.start();
        log.info("{},CNAPSCollectConsumer消费者启动", consumer.getInstanceName());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {
                startConsume();
            }
            catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            catch (MQClientException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
