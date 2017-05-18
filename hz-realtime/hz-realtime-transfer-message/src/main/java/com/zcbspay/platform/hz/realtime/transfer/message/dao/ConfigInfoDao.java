package com.zcbspay.platform.hz.realtime.transfer.message.dao;

import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;
import com.zcbspay.platform.hz.realtime.transfer.message.pojo.ConfigInfoDO;

public interface ConfigInfoDao extends BaseDAO<ConfigInfoDO> {

    public ConfigInfoDO getparamByName(String paramName);
}
