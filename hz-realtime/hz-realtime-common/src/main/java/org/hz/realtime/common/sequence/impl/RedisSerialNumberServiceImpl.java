/* 
 * RedisSerialNumberServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年9月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package org.hz.realtime.common.sequence.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hz.realtime.common.sequence.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月12日 下午3:50:14
 * @since
 */
@Service("redisSerialNumberService")
public class RedisSerialNumberServiceImpl implements SerialNumberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String HZ_COM_REF_ID_KEY = "SEQUENCE:CMBCSERIALNO";

    public String formateSequence(String key) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        Long increment = opsForValue.increment(key, 1);
        if (increment >= 99999999) {
            opsForValue.set(key, "0");
        }
        DecimalFormat df = new DecimalFormat("00000000");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String seqNo = sdf.format(new Date()) + df.format(increment);
        return seqNo;
    }

    @Override
    public String generateHZComuRefId() {
        String seqNo = formateSequence(HZ_COM_REF_ID_KEY);
        return seqNo;
    }

}
