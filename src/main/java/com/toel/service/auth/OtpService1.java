package com.toel.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.toel.service.ServiceToel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService1 {
    @Autowired
    ServiceToel servicetoel;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final long OTP_EXPIRATION_MINUTES = 5;
    private HashOperations<String, String, Object> hashOperations;

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public void storeUserBehavior(String userId, String productId, String actionType, String device,
            String location) {
        // String key = "USER_BEHAVIOR:" + userId + ":" + UUID.randomUUID().toString();
        String key = "USER_BEHAVIOR:" + userId;
        Map<String, Object> behaviorData = new HashMap<>();
        behaviorData.put("user_id", userId);
        behaviorData.put("product_id", productId);
        behaviorData.put("action_type", actionType);
        // behaviorData.put("action_time", actionTime.getTime());
        behaviorData.put("device", device);
        behaviorData.put("location", location);

        hashOperations.putAll(key, behaviorData);
    }

    // New method to retrieve user behavior data
    public List<Map<String, Object>> getUserBehavior(String userId) {
        String pattern = "USER_BEHAVIOR:" + userId + ":*";
        Set<String> keys = redisTemplate.keys(pattern);
        List<Map<String, Object>> behaviors = new ArrayList<>();

        if (keys != null) {
            for (String key : keys) {
                Map<String, Object> behaviorData = hashOperations.entries(key);
                behaviors.add(behaviorData);
            }
        }

        return behaviors;
    }

    // New method to retrieve all user behavior data
    public List<Map<String, Object>> getAllUserBehavior() {
        String pattern = "USER_BEHAVIOR:*";
        Set<String> keys = redisTemplate.keys(pattern);
        List<Map<String, Object>> behaviors = new ArrayList<>();

        if (keys != null) {
            for (String key : keys) {
                Map<String, Object> behaviorData = hashOperations.entries(key);
                behaviors.add(behaviorData);
            }
        }

        return behaviors;
    }

    // ..>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    @Autowired
    public OtpService1(RedisTemplate<String, Object> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    // ... (keep all existing methods)

    // public Map<String,Map<String, Object>> getAllData() {
    // String key = "USER:" + userId;
    // return hashOperations.;
    // }

    // New method to save user data
    public void saveUserData(String userId, Map<String, Object> userData) {
        String key = "USER:" + userId;
        hashOperations.putAll(key, userData);
    }

    // New method to get user data
    public Map<String, Object> getUserData(String userId) {
        String key = "USER:" + userId;
        return hashOperations.entries(key);
    }

    // New method to update specific fields of user data
    public void updateUserData(String userId, Map<String, Object> updatedFields) {
        String key = "USER:" + userId;
        hashOperations.putAll(key, updatedFields);
    }

    // New method to delete user data
    public void deleteUserData(String userId) {
        String key = "USER:" + userId;
        redisTemplate.delete(key);
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public String setData(String key, List<String> data) {
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

    public String find(String email) {
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