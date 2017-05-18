package com.zcbspay.platform.hz.realtime.transfer.message.dao.impl;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.hz.realtime.transfer.message.dao.ConfigInfoDao;
import com.zcbspay.platform.hz.realtime.transfer.message.pojo.ConfigInfoDO;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月22日 下午1:45:26
 * @since
 */
@Repository
public class ConfigInfoDAOImpl extends HibernateBaseDAOImpl<ConfigInfoDO> implements ConfigInfoDao {

    private static final Logger log = LoggerFactory.getLogger(ConfigInfoDAOImpl.class);

    @Override
    @Transactional(readOnly = true)
    public ConfigInfoDO getparamByName(String paramName) {
        String hql = "from ConfigInfoDO where paraname=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, paramName);
        return (ConfigInfoDO) query.uniqueResult();
    }

}
