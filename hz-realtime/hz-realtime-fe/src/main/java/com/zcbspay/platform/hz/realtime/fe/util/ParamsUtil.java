package com.zcbspay.platform.hz.realtime.fe.util;

import java.util.ResourceBundle;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcbspay.platform.hz.realtime.common.utils.date.DateUtil;

/**
 * 定时加载properties文件参数
 * 
 * @author AlanMa
 *
 */
public class ParamsUtil {

    private static final Logger log = LoggerFactory.getLogger(ParamsUtil.class);

    private String hzqszx_ip;
    private String hzqszx_port;
    private String head_length;

    /*
     * 工具运行参数
     */
    private boolean canRun;
    private String refresh_interval;
    private static ParamsUtil constant;
    // private String path = "/home/web/trade/unionpay/";
    // private String fileName = "unionpay_params.properties";
    private static ResourceBundle RESOURCE;

    public static synchronized ParamsUtil getInstance() {
        if (constant == null) {
            constant = new ParamsUtil();
        }
        return constant;
    }

    private ParamsUtil() {
        refresh();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (canRun) {
                    try {
                        refresh();
                        int interval = NumberUtils.toInt(refresh_interval, 60) * 1000;// 刷新间隔，单位：秒
                        log.info("refresh Constant datetime:" + DateUtil.getCurrentDateTime());
                        Thread.sleep(interval);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void refresh() {
        RESOURCE = ResourceBundle.getBundle("gateway");
        hzqszx_ip = RESOURCE.getString("hzqszx_ip");
        hzqszx_port = RESOURCE.getString("hzqszx_port");
        head_length = RESOURCE.getString("head_length");

        canRun = true;
        refresh_interval = RESOURCE.getString("refresh_interval");
    }

    public String getHzqszx_ip() {
        return hzqszx_ip;
    }

    public void setHzqszx_ip(String hzqszx_ip) {
        this.hzqszx_ip = hzqszx_ip;
    }

    public String getHzqszx_port() {
        return hzqszx_port;
    }

    public void setHzqszx_port(String hzqszx_port) {
        this.hzqszx_port = hzqszx_port;
    }

    public String getRefresh_interval() {
        return refresh_interval;
    }

    public void setRefresh_interval(String refresh_interval) {
        this.refresh_interval = refresh_interval;
    }

    public String getHead_length() {
        return head_length;
    }

    public void setHead_length(String head_length) {
        this.head_length = head_length;
    }

}
