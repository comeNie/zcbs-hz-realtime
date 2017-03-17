/* 
 * SerialNumberService.java  
 * 
 * version TODO
 *
 * 2016年9月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.hz.realtime.common.sequence;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月12日 下午3:49:23
 * @since
 */
public interface SerialNumberService {

    /**
     * 生成杭州清算中心通信参考号（16位）
     * 
     * @return 通信参考号
     */
    public String generateHZComuRefId();

}
