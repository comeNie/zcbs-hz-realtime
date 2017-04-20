package com.zcbspay.platform.hz.realtime.business.message.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.hz.realtime.business.message.dao.RspmsgDAO;
import com.zcbspay.platform.hz.realtime.business.message.pojo.RspmsgDO;
import com.zcbspay.platform.hz.realtime.business.message.service.enums.ChnlTypeEnum;
import com.zcbspay.platform.hz.realtime.common.dao.impl.HibernateBaseDAOImpl;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月22日 下午1:45:26
 * @since 
 */
@Repository
public class RspmsgDAOImpl  extends HibernateBaseDAOImpl<RspmsgDO> implements RspmsgDAO{
    private static final Logger log=LoggerFactory.getLogger(RspmsgDAOImpl.class);
    
    /**
     *
     * @param rspid
     * @return
     */
    @Override
    @Transactional(readOnly=true)
    public RspmsgDO get(String rspid) {
        RspmsgDO rspmsg = (RspmsgDO) getSession().get(RspmsgDO.class, rspid);
        if(rspmsg!=null){
            getSession().evict(rspmsg);
        }
        return rspmsg;
    }

    @SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public RspmsgDO getRspmsgByChnlCode(ChnlTypeEnum chnlType,String retCode) {
        List<RspmsgDO> result = null;
        String  queryString = null;
        if(chnlType==null){
        	queryString = "from RspmsgDO where chnlrspcode=?";
        }else{
        	queryString = "from RspmsgDO where chnltype=? and chnlrspcode=?";
        }
        try {
            log.info("getRspmsgByChnlCode() queryString:"+queryString);
            Query query = getSession().createQuery(queryString);
            if(chnlType==null){
            	query.setParameter(0, retCode);
            }else{
            	query.setParameter(0,chnlType.getTradeType());
                query.setParameter(1, retCode);
            }
            result = query.list();
            if(result.size()>0){
                return result.get(0);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return null;
    }

}
