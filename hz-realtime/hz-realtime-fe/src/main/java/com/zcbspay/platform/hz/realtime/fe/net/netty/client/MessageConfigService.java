package com.zcbspay.platform.hz.realtime.fe.net.netty.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcbspay.platform.hz.realtime.common.constant.Constant;

/**
 * <strong>Title : MessageConfigService</strong><br>
 * <strong>Description : 报文配置服务</strong><br>
 * <strong>Create on : 2016-3-15</strong><br>
 * 
 * @author linda1@cmbc.com.cn<br>
 */
public class MessageConfigService {

    /**
     * 日志对象
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 配置参数集合
     */
    private final Map<String, Object> configParams = new HashMap<String, Object>();

    /**
     * 允许运行标识
     */
    private volatile boolean canRun = true;

    /**
     * 初始化
     */
    public void init() {
        // 加载参数
        refresh();
        /**
         * 运行内存刷新线程
         */
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (canRun) {
                    try {
                        refresh();
                        int interval = NumberUtils.toInt((String) configParams.get("REFRESH_INTERVAL"), 60) * 1000;// 刷新间隔，单位：秒
                        Thread.sleep(interval);
                    }
                    catch (Exception e) {
                        logger.error(e.getLocalizedMessage(), e);
                    }
                }
            }
        }).start();
    }

    /**
     * 刷新参数
     */
    private void refresh() {
        // 读取参数功能自行实现
        String canRunStr = (String) configParams.get("CAN_RUN");// 是否允许运行标识
        if ("false".equalsIgnoreCase(canRunStr)) {
            canRun = false;
        }
        else {
            canRun = true;
        }
        configParams.put("CAN_RUN", String.valueOf(canRun));
        String hostName = StringUtils.trimToNull((String) configParams.get("HOST_NAME"));// 主机名称
        if (hostName == null) {
            configParams.put("HOST_NAME", "未知");
        }
        String hostAddress = StringUtils.trimToNull((String) configParams.get("HOST_ADDRESS"));// 主机地址
        if (hostAddress == null) {
            configParams.put("HOST_ADDRESS", Constant.getInstance().getHzqszx_ip());
        }
        String hostPort = StringUtils.trimToNull((String) configParams.get("HOST_PORT"));// 主机端口
        if (hostPort == null) {
            configParams.put("HOST_PORT", Constant.getInstance().getHzqszx_port() + "");
        }
        String headLength = StringUtils.trimToNull((String) configParams.get("HEAD_LENGTH"));// 报文头长度
        if (headLength == null) {
            configParams.put("HEAD_LENGTH", Constant.getInstance().getHeadLength() + "");
        }
        String charset = StringUtils.trimToNull((String) configParams.get("CHARSET"));// 字符集
        if (charset == null) {
            configParams.put("CHARSET", "UTF-8");
        }
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Object set(String key, Object value) {
        return configParams.put(key, value);
    }

    /**
     * @param key
     * @return
     */
    public Object getObject(String key) {
        return configParams.get(key);
    }

    /**
     * @param key
     * @return
     */
    public String getString(String key) {
        Object obj = configParams.get(key);
        if (obj == null) {
            return null;
        }
        return StringUtils.trimToNull(String.valueOf(configParams.get(key)));
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        Object obj = configParams.get(key);
        if (obj == null) {
            return defaultValue;
        }
        String value = StringUtils.trimToNull(String.valueOf(configParams.get(key)));
        return (value != null ? value : defaultValue);
    }

    /**
     * @param key
     * @return
     */
    public int getInt(String key) {
        Object value = configParams.get(key);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return NumberUtils.toInt(String.valueOf(configParams.get(key)));
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        Object value = configParams.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return NumberUtils.toInt(String.valueOf(configParams.get(key)), defaultValue);
    }

    /**
     * @param key
     * @return
     */
    public long getLong(String key) {
        Object value = configParams.get(key);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return NumberUtils.toLong(String.valueOf(configParams.get(key)));
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLong(String key, long defaultValue) {
        Object value = configParams.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return NumberUtils.toLong(String.valueOf(configParams.get(key)), defaultValue);
    }

    /**
     * @param key
     * @return
     */
    public double getDouble(String key) {
        Object value = configParams.get(key);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return NumberUtils.toDouble(String.valueOf(configParams.get(key)));
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public double getDouble(String key, double defaultValue) {
        Object value = configParams.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return NumberUtils.toDouble(String.valueOf(configParams.get(key)), defaultValue);
    }

    /**
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        Object value = configParams.get(key);
        if (value == null) {
            return false;
        }
        return BooleanUtils.toBoolean(String.valueOf(configParams.get(key)));
    }
}
