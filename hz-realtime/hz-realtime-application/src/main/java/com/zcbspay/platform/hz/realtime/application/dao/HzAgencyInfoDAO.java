package com.zcbspay.platform.hz.realtime.application.dao;

import com.zcbspay.platform.hz.realtime.application.pojo.HzAgencyInfoDO;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;

public interface HzAgencyInfoDAO extends BaseDAO<HzAgencyInfoDO> {

    /**
     * 通过商户号和业务代码获取渠道参数信息
     * @param merchno
     * @param busicode
     * @return
     */
    public HzAgencyInfoDO getHzAgencyInfoByMerIdBusTyp(String merchno, String busicode);
}
