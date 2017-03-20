package com.zcbspay.platform.hz.realtime.fe.net.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.fe.net.netty.SocketChannelHelper;

/**
 * 杭州清算中心回调Server服务启动监听器
 *
 * @author alanma
 * @version
 * @date 2016年10月14日 上午10:43:24
 * @since
 */
@Service
public class ListenerHZQSZX implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(ListenerHZQSZX.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            SocketChannelHelper.getInstance();
            log.info("ListenerHZQSZX onApplicationEvent netty server is started~~~~~~");
        }
    }

}
