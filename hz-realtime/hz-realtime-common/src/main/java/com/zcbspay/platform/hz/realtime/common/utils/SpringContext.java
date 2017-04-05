package com.zcbspay.platform.hz.realtime.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月6日 下午5:57:57
 * @since
 */
@Component
public class SpringContext implements ApplicationContextAware {

    protected static ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

}
