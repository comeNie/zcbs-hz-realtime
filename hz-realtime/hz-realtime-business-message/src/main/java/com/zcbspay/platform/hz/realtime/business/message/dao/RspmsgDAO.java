/* 
 * RspmsgDAO.java  
 * 
 * version TODO
 *
 * 2015年10月22日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.hz.realtime.business.message.dao;

import com.zcbspay.platform.hz.realtime.business.message.pojo.RspmsgDO;
import com.zcbspay.platform.hz.realtime.business.message.service.enums.ChnlTypeEnum;
import com.zcbspay.platform.hz.realtime.common.dao.BaseDAO;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月22日 下午1:43:04
 * @since 
 */
public interface RspmsgDAO extends BaseDAO<RspmsgDO>{

    public RspmsgDO get(String rspid);
    public RspmsgDO getRspmsgByChnlCode(ChnlTypeEnum chnlType,String retCode) ;
}
