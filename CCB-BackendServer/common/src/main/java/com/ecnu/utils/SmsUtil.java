package com.ecnu.utils;


import com.ecnu.constant.RedisConstant;
import com.ecnu.dto.SmsDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SmsUtil {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisTokenBucket redisTokenBucket;
    @Autowired
    private RabbitMqUtil rabbitMqUtil;

    public boolean sendSms(SmsDTO smsDTO) {
        // 从令牌桶中取得令牌，未取得不允许发送短信
        boolean acquire = redisTokenBucket.tryAcquire(smsDTO.getPhoneNumber());
        if (!acquire) {
            log.info("phoneNum：{}，send SMS frequent", smsDTO.getPhoneNumber());
            return false;
        }
        log.info("发送短信：{}",smsDTO);
        String phoneNum = smsDTO.getPhoneNumber();
        String code = smsDTO.getCode();

        // 将手机号对应的验证码存入Redis，方便后续检验
        redisTemplate.opsForValue().set(RedisConstant.SMS_CODE_PREFIX + phoneNum, String.valueOf(code), 5, TimeUnit.MINUTES);

        // 利用消息队列，异步发送短信
        rabbitMqUtil.sendSmsAsync(smsDTO);
        return true;
    }

    public boolean verifyCode(String phoneNum, String code) {
        String key = RedisConstant.SMS_CODE_PREFIX + phoneNum;
        String checkCode = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(code) && code.equals(checkCode)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;

    }

}
