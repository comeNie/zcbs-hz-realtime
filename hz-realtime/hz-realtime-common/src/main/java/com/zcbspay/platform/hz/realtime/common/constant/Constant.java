package com.zcbspay.platform.hz.realtime.common.constant;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcbspay.platform.hz.realtime.common.utils.date.DateUtil;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月13日 上午9:32:56
 * @since
 */
public class Constant {

    private static final Logger log = LoggerFactory.getLogger(Constant.class);

    private String hzqszx_ip;
    private String hzqszx_port;
    private String headLength;
    private boolean canRun;
    private String refresh_interval;
    private static Constant constant;

    public static synchronized Constant getInstance() {
        if (constant == null) {
            constant = new Constant();
        }
        return constant;
    }

    private Constant() {
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
        try {
            String path = "/home/web/trade/";
            File file = new File(path + "zlrt.properties");
            if (!file.exists()) {
                path = getClass().getResource("/").getPath();
                file = null;
            }
            Properties prop = new Properties();
            InputStream stream = null;
            stream = new BufferedInputStream(new FileInputStream(new File(path + "zlrt.properties")));
            prop.load(stream);
            hzqszx_ip = prop.getProperty("hzqszx_ip");
            hzqszx_port = prop.getProperty("hzqszx_port");
            canRun = true;
            refresh_interval = prop.getProperty("refresh_interval");
        }
        catch (FileNotFoundException e) {
            log.error("FileNotFoundException", e);
        }
        catch (IOException e) {
            log.error("IOException", e);
        }
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

    public boolean isCanRun() {
        return canRun;
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }

    public String getRefresh_interval() {
        return refresh_interval;
    }

    public String getHeadLength() {
        return headLength;
    }

    public void setHeadLength(String headLength) {
        this.headLength = headLength;
    }

    public void setRefresh_interval(String refresh_interval) {
        this.refresh_interval = refresh_interval;
    }

}
