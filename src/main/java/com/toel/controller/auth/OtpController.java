package com.toel.controller.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.toel.model.Account;
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
    @Autowired
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();// Create BCryptPasswordEncoder instance

    @PostMapping("/api/v1/otp/generate")
    public ResponseEntity<String> generateOtp(@RequestParam String email) {
        // boolean isvalid = accountRepository.existsByEmail(email);
        boolean isvalid = true;
        if (isvalid) {
            String otp = otpService.generateOtp(email);
            // emailService.sendSimpleEmail("kienlhpc05751@fpt.edu.vn", "Test Subject",
            // "Test Email Body"+otp);
            // emailService.push("kienlhpc05751@fpt.edu.vn", "Mã otp của bạn", otp);
            emailService.push("kienlhpc05751@fpt.edu.vn", "Mã otp của bạn", EmailTemplateType.OTP, otp, "lỏ");
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

    @PostMapping("/user/updatePass") // updatePass
    public ResponseEntity<?> putMethodName(@RequestParam Integer id, @RequestParam String repass,
            @RequestParam String oldpass) {
        Optional<Account> account = accountRepository.findById(id);
        Account accountRe = account.get();
        String hashPass = passwordEncoder.encode(repass);
        accountRe.setPassword(hashPass);
        try {
            accountRepository.save(accountRe);
        } catch (Exception e) {
            // TODO: handle exceptiot
            e.printStackTrace();
            return ResponseEntity.badRequest().body("lỗi");
        }
        return ResponseEntity.ok("oke");
    }
   
    @PostMapping("/user/changePassword") // email trung se sai
    public ResponseEntity<?> changePassword(
            @RequestParam String pass,
            @RequestParam String rpass,
            @RequestParam String email) {
        if (!pass.equals(rpass)) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }
        Account accountOpt = accountRepository.findByEmail(email);
        if (accountOpt == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }
        // Account account = accountOpt.get();
        // Set the new password (You should hash the password before saving)
        accountOpt.setPassword(passwordEncoder.encode(pass));
        accountRepository.save(accountOpt);
        return ResponseEntity.ok("Password changed successfully.");
    }
}