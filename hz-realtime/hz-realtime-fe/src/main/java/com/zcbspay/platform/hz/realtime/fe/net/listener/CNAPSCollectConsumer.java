package com.zcbspay.platform.hz.realtime.fe.net.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.zcbspay.platform.hz.realtime.fe.net.netty.SocketChannelHelper;

/**
 * 民生代扣渠道消费者
 *
 * @author guojia
 * @version
 * @date 2016年10月14日 上午10:43:24
 * @since
 */
@Service
public class CNAPSCollectConsumer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(CNAPSCollectConsumer.class);
    @Autowired
    @Qualifier("cnapsCollectListener")
    private MessageListenerConcurrently simpleOrderListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            SocketChannelHelper.getInstance();
            log.info("CNAPSCollectConsumer onApplicationEvent netty server is started~~~~~~");
        }
    }

}
