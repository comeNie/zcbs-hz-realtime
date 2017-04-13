package com.zcbspay.platform.hz.realtime.business.message.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.zcbspay.platform.hz.realtime.business.message.BaseTest;
import com.zcbspay.platform.hz.realtime.business.message.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.TTxnsLogDO;
import com.zcbspay.platform.hz.realtime.message.bean.CMS900Bean;

public class TestDao extends BaseTest {

    @Autowired
    private TChnCollectSingleLogDAO tChnCollectSingleLogDAO;
    @Autowired
    private TChnPaymentSingleLogDAO tChnPaymentSingleLogDAO;
    @Autowired
    private TxnsLogDAO txnsLogDAO;
    @Autowired
    private OrderCollectSingleDAO orderCollectSingleDAO;

    @Test
    public void testAll() {
        long currentTime = System.currentTimeMillis();
        System.out.println("excute time:" + (System.currentTimeMillis() - currentTime));
    }

    private void updateRealCollectLogCommResp() {
        String json = "{\"MsgId\":\"20170405128724\",\"OrgnlMsgId\":{\"OrgnlMsgId\":\"2017040500000106\",\"OrgnlMsgType\":\"CMS900\",\"OrgnlSender\":\"XXXXXXXXXX\"},\"RspnInf\":{\"NetgDt\":\"20170405\",\"RjctCd\":\"I000\",\"RjctInf\":\"处理成功\",\"Sts\":\"S\"}}";
        CMS900Bean bean = JSONObject.parseObject(json, CMS900Bean.class);
        tChnCollectSingleLogDAO.updateRealCollectLogCommResp(bean);

    }

    private void updateRealPaymentLogCommResp() {
        String json = "{\"MsgId\":\"20170405128724\",\"OrgnlMsgId\":{\"OrgnlMsgId\":\"2017040500000106\",\"OrgnlMsgType\":\"CMS900\",\"OrgnlSender\":\"XXXXXXXXXX\"},\"RspnInf\":{\"NetgDt\":\"20170405\",\"RjctCd\":\"I000\",\"RjctInf\":\"处理成功\",\"Sts\":\"S\"}}";
        CMS900Bean bean = JSONObject.parseObject(json, CMS900Bean.class);
        tChnPaymentSingleLogDAO.updateRealPaymentLogCommResp(bean);
    }

    private void getTxnsLogByTxnseqno() {
        TTxnsLogDO tTxnsLogDO = txnsLogDAO.getTxnsLogByTxnseqno("1604209900054111");
        System.out.println("~~~~~~~~:" + JSONObject.toJSONString(tTxnsLogDO));
    }

}
