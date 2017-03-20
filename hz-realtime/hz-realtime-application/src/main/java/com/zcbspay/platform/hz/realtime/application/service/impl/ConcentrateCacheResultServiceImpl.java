/* 
 * InsteadPayCacheResultServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年10月19日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.hz.realtime.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.application.service.ConcentrateCacheResultService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月19日 下午2:42:20
 * @since 
 */
@Service
public class ConcentrateCacheResultServiceImpl implements ConcentrateCacheResultService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	/**
	 *
	 * @param key
	 * @param json
	 */
	@Override
	public void saveInsteadPayResult(String key, String json) {
		// TODO Auto-generated method stub
		/*ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
		opsForValue.set(key, json, 10, TimeUnit.MINUTES);*/
		BoundListOperations<String, Object> boundListOps = redisTemplate.boundListOps(key);
		boundListOps.leftPush(json);
	}

}
