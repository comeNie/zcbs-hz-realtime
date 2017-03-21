package com.zcbspay.platform.hz.realtime.common.sequence.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.hz.realtime.common.sequence.SerialNumberService;

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
    private static final String HZ_COM_REF_ID_KEY = "SEQUENCE:HZCOMUREFID";
    private static final String DB_PRIMARY_KEY = "SEQUENCE:DBPRIMARYKEY";
    private static final String HZ_MESSAGE_ID = "SEQUENCE:HZMESSAGEID";
    private static final String HZ_TRAN_IDDEN = "SEQUENCE:HZTRANIDDEN";

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

    @Override
    public Long generateDBPrimaryKey() {
        String seqNo = formateSequence(DB_PRIMARY_KEY);
        return Long.parseLong(seqNo.substring(4, 16));
    }

    @Override
    public String generateHZMsgId() {
        String seqNo = formateSequence(HZ_MESSAGE_ID);
        return seqNo;
    }

    @Override
    public String generateTranIden() {
        String seqNo = formateSequence(HZ_TRAN_IDDEN);
        return seqNo;
    }
}
