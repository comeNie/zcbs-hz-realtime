package com.zcbspay.platform.hz.realtime.application.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.hz.realtime.application.dao.HzAgencyInfoDAO;
import com.zcbspay.platform.hz.realtime.application.pojo.HzAgencyInfoDO;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;

@Repository
public class HzAgencyInfoDAOImpl extends HibernateBaseDAOImpl<HzAgencyInfoDO> implements HzAgencyInfoDAO {

    // private static final Logger log =
    // LoggerFactory.getLogger(HzAgencyInfoDAOImpl.class);

    @Override
    @Transactional(readOnly = true)
    public HzAgencyInfoDO getHzAgencyInfoByMerIdBusTyp(String merchno, String busicode) {
        String hql = "from HzAgencyInfoDO where merchno=? and busicode=?";
        Query query = getSession().createQuery(hql);
        query.setString(0, merchno);
        query.setString(1, busicode);
        return (HzAgencyInfoDO) query.uniqueResult();
    }

}