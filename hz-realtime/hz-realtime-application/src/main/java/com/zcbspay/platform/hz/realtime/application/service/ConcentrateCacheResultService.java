/* 
 * InsteadPayCacheResultService.java  
 * 
 * version TODO
 *
 * 2016年10月19日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.hz.realtime.application.service;

/**
 * 代付结果缓存service
 *
 * @author guojia
 * @version
 * @date 2016年10月19日 下午2:41:10
 * @since 
 */
public interface ConcentrateCacheResultService {

	/**
	 * 将消费者执行后的结果缓存到redis中
	 * @param key redis主键
	 * @param json JSON结果
	 */
	public void saveInsteadPayResult(String key,String json);
}
