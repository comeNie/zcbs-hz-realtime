package org.hz.realtime.fe.net.netty;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hz.realtime.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * <strong>Title : MessageHandler</strong><br>
 * <strong>Description : 报文处理器</strong><br>
 * <strong>Create on : 2015-9-30</strong><br>
 * 
 * @author linda1@cmbc.com.cn<br>
 */
public class MessageHandler {

    /**
     * 日志对象
     */
    private final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    /**
     * 报文配置服务
     */
    private MessageConfigService messageConfigService;

    /**
     * 请求消息映射集合
     */
    private final Map<String, Element> reqMsgMapping = new ConcurrentHashMap<String, Element>();

    /**
     * 响应消映射集合
     */
    private final Map<String, Element> resMsgMapping = new ConcurrentHashMap<String, Element>();

    /**
     * @param messageConfigService
     *            the messageConfigService to set
     */
    public void setMessageConfigService(MessageConfigService messageConfigService) {
        this.messageConfigService = messageConfigService;
    }

    /**
     * 启动
     * 
     * @return
     */
    public void init() {
        /*
         * try { // 加载请求报文域配置 this.loadConfig("REQUEST",
         * messageConfigService.getString("MSG_CFG_PATH_REQ")); // 加载响应报文域配置
         * this.loadConfig("RESPONSE",
         * messageConfigService.getString("MSG_CFG_PATH_RES")); } catch
         * (Exception e) { logger.error(e.getLocalizedMessage(), e); }
         */
    }

    /**
     * 加载配置文件
     * 
     * @param messageNature
     *            报文性质
     * @param inputStream
     *            文件输入流
     * @throws Exception
     */
    protected void loadConfig(String messageNature, String filePath) throws Exception {
        Map<String, Element> mapping = reqMsgMapping;
        if ("RESPONSE".equalsIgnoreCase(messageNature)) {
            mapping = resMsgMapping;
        }

        String classpathKey = "classpath:";
        InputStream inputStream = null;
        if (filePath.startsWith(classpathKey)) {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath.substring(classpathKey.length()));
        }
        else {
            inputStream = new FileInputStream(filePath);
        }

        SAXReader reader = new SAXReader();
        Document doc = reader.read(inputStream);
        Element root = doc.getRootElement();
        for (Element element : (List<Element>) root.elements()) {
            String messageId = element.attributeValue("id");
            if (!mapping.containsKey(messageId)) {
                mapping.put(messageId, element);
            }
            else {
                logger.error("报文[{}]配置已存在", new Object[] { messageId });
            }
        }
        reader = null;
        mapping = null;
        doc = null;
    }

    public Map<String, Object> unpack(byte[] bodyBytes) {
        // TODO mxwtodo
        return null;
    }

}
