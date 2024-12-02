package com.toel.controller.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.toel.dto.ChangePassOtp;
import com.toel.model.Account;
import com.toel.repository.AccountRepository;
import com.toel.service.ServiceToel;
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
    ServiceToel serviceToel;
    @Autowired
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/api/v1/otp/generate")
    public ResponseEntity<String> generateOtp(@RequestBody Account entity) {
        System.out.println("email" + entity.getEmail());
        boolean isvalid = accountRepository.existsByEmail(entity.getEmail());
        // boolean isvalid = true;
        if (isvalid) {
            String otp = otpService.generateOtp(entity.getEmail());
            String hashOTP = serviceToel.hashPassword(otp);
            System.out.println("otp nè: " + otp + " hashOTP " + hashOTP + " , mail " + entity.getEmail() + isvalid);

            emailService.push(entity.getEmail(), "Quên mật khẩu ", EmailTemplateType.OTP, otp,
                    "http://localhost:5173/change-password?otp=" + otp);
            return ResponseEntity.ok("OTP generated: " + otp);

        } else {
            return ResponseEntity.ok("emal không tồn tại ! ");

        }
    }

    @PostMapping("api/v1/otp/lo")
    public String postMethodName(@RequestParam String email) {
        String o = otpService.find(email);
        return o;
    }

    @PostMapping("/api/v1/otp/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody ChangePassOtp entity) {
        try {
            boolean isValid = otpService.verifyOtp(entity.getEmail(), entity.getOtp());
            boolean accountCheck = accountRepository.existsByEmail(entity.getEmail());
            Account account = accountRepository.findByEmail(entity.getEmail());
            String encryptedPassword = passwordEncoder.encode(entity.getNewpass());
            if (!accountCheck) {
                return ResponseEntity.badRequest().body("email không tồn tại!");
            }
            if (isValid) {
                account.setPassword(encryptedPassword);
                accountRepository.save(account);
                emailService.push(account.getEmail(), "ĐỔI MẬT KHẨU THÀNH CÔNG !", EmailTemplateType.PASSWORD_SUSSECC,
                        account.getFullname());
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                return ResponseEntity.badRequest().body("Invalid OTP !");
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return ResponseEntity.badRequest().body("error");
        }

    }

    // register v2
    @PostMapping("/api/v2/user/register/generateOTP")
    public ResponseEntity<String> registerOTPV2(@RequestBody Account entity,
            @RequestParam(defaultValue = "false") Boolean style) {
        if (!style) {
            System.out.println("email" + entity.getEmail());
            boolean isvalid = accountRepository.existsByEmail(entity.getEmail());
            // boolean isvalid = true;
            if (!isvalid) {
                String otp = otpService.generateOtp(entity.getEmail());
                String hashOTP = serviceToel.hashPassword(otp);
                System.out.println("otp nè: " + otp + " hashOTP " + hashOTP + " , mail " + entity.getEmail() + isvalid);

                emailService.push(entity.getEmail(), "Đăng ký tài khoản", EmailTemplateType.DANGKYTAIKHOAN, otp,
                        "http://localhost:5173/singup2?otp=" + otp + "&email=" + entity.getEmail());
                return ResponseEntity.ok("OTP generated: " + otp);

            } else {
                return ResponseEntity.badRequest().body("Email đã tồn tại ! ");    

            }
        } else {
            return ResponseEntity.ok("Số điện thoại không tồn tại ! ");
        }

        // String otp = otpService.generateOtp(email);
        // return ResponseEntity.ok("OTP generated: " + otp);
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

    @PostMapping("/api/v1/user/updatePass") // updatePass
    public ResponseEntity<?> putMethodName(@RequestParam Integer id, @RequestParam String repass,
            @RequestParam String oldpass) {
        try {
            Optional<Account> account = accountRepository.findById(id);
            if (account.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("lỗi 400!");
            }
            Account accountRe = account.get();
            // So sánh mật khẩu cũ nhập vào với mật khẩu đã mã hóa trong cơ sở dữ liệu
            if (!passwordEncoder.matches(oldpass, accountRe.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("sai mật khẩu!");
            }
            // Mã hóa mật khẩu mới
            String hashPass = passwordEncoder.encode(repass);
            accountRe.setPassword(hashPass);
            accountRepository.save(accountRe);
            return ResponseEntity.ok("Cập nhật mật khẩu thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cập nhật mật khẩu không thành công!");
        }

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