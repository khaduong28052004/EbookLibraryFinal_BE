package com.toel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter@Setter 
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassOtp {
     private String email;
     private String otp;
     private String newpass;

}
