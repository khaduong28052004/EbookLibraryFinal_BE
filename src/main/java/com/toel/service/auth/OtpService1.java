package com.toel.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.toel.service.ServiceToel;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService1 {
 @Autowired
 ServiceToel servicetoel;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final long OTP_EXPIRATION_MINUTES = 5; 

    public String setData(String key,List<String> data){
        redisTemplate.opsForValue().getAndSet(key, data);
        return "Thanh cong";
    }

    public List<String> getData(String key) {
        Object data = redisTemplate.opsForValue().get(key);
        if (data != null && data instanceof List<?>) {
            return (List<String>) data;
        }
        return null; // Or handle the case when the data is not found
    }

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));

        String key = "OTP:" + email;
        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        System.out.println(otp + " h");
        return otp;
        // return "oke";
    }

    public boolean verifyOtp(String email, String otp) {
        String key = "OTP:" + email;
        String storedOtp = (String) redisTemplate.opsForValue().get(key);
        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
    public String find(String email  ){
        String key = "OTP:" + email;
        String storedOtp = (String) redisTemplate.opsForValue().get(key);
        return storedOtp;
    }

    public void deleteOtp(String email) {
        String key = "OTP:" + email;
        redisTemplate.delete(key);
    }

    public long getOtpExpirationTime(String email) {
        String key = "OTP:" + email;
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}