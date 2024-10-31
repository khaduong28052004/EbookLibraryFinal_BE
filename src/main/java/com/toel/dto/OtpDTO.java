package com.toel.dto;

import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("OtpDTO")
public class OtpDTO implements Serializable {
    
    // private static final long serialVersionUID = 1L;

    private Integer id;
    private String email;
    private String otp;
    private String expiryTime;
}
