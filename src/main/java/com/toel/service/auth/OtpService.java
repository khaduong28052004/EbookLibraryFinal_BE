package com.toel.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.toel.service.ServiceToel;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {
 @Autowired
 ServiceToel servicetoel;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final long OTP_EXPIRATION_MINUTES = 5; 

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        String hashOtp = servicetoel.hashPassword(otp);
        String key = "OTP:" + email;
        redisTemplate.opsForValue().set(key, hashOtp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        System.out.println(otp + " hash " + hashOtp);
        return hashOtp;
        // return "oke";
    }

    public String generateOtp1(String email) {
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

    
    public String generateMapdata(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        String key = "OTP:" + email;
        Map<String,Object> map = new HashMap<>();
        redisTemplate.opsForValue().set(key, map, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        System.out.println(otp + " h");
        return otp;
        // return "oke";
    }
}