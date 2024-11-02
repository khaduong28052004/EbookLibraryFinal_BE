package com.toel.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.toel.repository.AccountRepository;
import com.toel.service.Email.EmailService;
import com.toel.service.Email.EmailTemplateType;
import com.toel.service.auth.OtpService;

@CrossOrigin("*")
@RestController
// @RequestMapping("/api/v1/user/otp")
public class OtpController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OtpService otpService;


    @PostMapping("/api/v1/otp/generate")
    public ResponseEntity<String> generateOtp(@RequestParam String email) {
        // boolean isvalid = accountRepository.existsByEmail(email);
        boolean isvalid = true;
        if (isvalid) {
            String otp = otpService.generateOtp(email);
            // emailService.sendSimpleEmail("kienlhpc05751@fpt.edu.vn", "Test Subject", "Test Email Body"+otp);
            //emailService.push("kienlhpc05751@fpt.edu.vn", "Mã otp của bạn", otp);
            emailService.push("kienlhpc05751@fpt.edu.vn", "Mã otp của bạn", EmailTemplateType.OTP, otp,"lỏ");
            return ResponseEntity.ok("OTP generated: " + otp);

        } else {
            return ResponseEntity.ok("emal không tồn tại ! ");

        }

        // String otp = otpService.generateOtp(email);
        // return ResponseEntity.ok("OTP generated: " + otp);
    }

    @PostMapping("api/v1/otp/lo")
    public String postMethodName(@RequestParam String email) {
        // TODO: process POST request
        String o = otpService.find(email);

        return o;
    }

    @PostMapping("/api/v1/otp/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
    }

    @Autowired
    private EmailService emailService;

    @GetMapping("/api/v1/otp/send-email")
    public String sendEmail() {
        // emailService.sendSimpleEmail("kienlhpc05751@fpt.edu.vn", "Test Subject",
        // "Test Email Body");
        emailService.push("kienlhpc05751@fpt.edu.vn", "Test Subject", "Test Email Body");
        return "Email sent!";
    }
}