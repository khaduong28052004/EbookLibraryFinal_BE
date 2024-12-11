package com.toel.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@org.springframework.stereotype.Service
public class ServiceToel {
    // public String hashPassword(String password) {
    // try {
    // MessageDigest md = MessageDigest.getInstance("SHA-256");
    // byte[] hashBytes = md.digest(password.getBytes());
    // return Base64.getEncoder().encodeToString(hashBytes);
    // } catch (NoSuchAlgorithmException e) {
    // e.printStackTrace();
    // return null;
    // }
    // }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            // Standard Base64 encoding
            String base64Hash = Base64.getEncoder().encodeToString(hashBytes);
            // Convert to URL-safe Base64
            return base64Hash.replace("+", "-").replace("/", "_").replace("=", "");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
