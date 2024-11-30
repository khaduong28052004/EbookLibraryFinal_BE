package com.toel.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;

import com.toel.model.Account;
import com.toel.repository.AccountRepository;
import com.toel.service.firebase.UploadImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class ApiUpdateAccount {
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UploadImage UploadImage;

	public ApiUpdateAccount(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

 @PutMapping("/user/updateAccount/{id}")
public ResponseEntity<Map<String, Object>> updateAccount(
        @PathVariable("id") Integer id, @RequestBody Account entity) {
    Map<String, Object> response = new HashMap<>();
    
    // Tìm tài khoản theo ID hoặc ném ra lỗi nếu không tìm thấy
    Account account = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
    
    // Cập nhật các trường thông tin từ entity vào tài khoản
    // account.setShopName(entity.getShopName());
    // account.setUsername(entity.getUsername());
    account.setFullname(entity.getFullname());
    account.setEmail(entity.getEmail());
    account.setBirthday(entity.getBirthday());
    account.setPhone(entity.getPhone());
    
    // Lưu thông tin cập nhật vào cơ sở dữ liệu
    accountRepository.save(account);
    // Trả về phản hồi thành công
    response.put("data", account);
    response.put("message", "Cập nhật tài khoản thành công");
    response.put("status", "success");
    
    return ResponseEntity.ok(response);
}
@GetMapping("/user/{id}")
public Account getAccountById(@PathVariable Integer id) {
    // Tìm và trả về tài khoản theo ID, ném lỗi nếu không tìm thấy
    return accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
}
// @PostMapping("/user/saveImg/{id}")
//     public Map<String, Object> updateRegister(@PathVariable("id") Integer id,
//             @RequestParam("imgAvartar") MultipartFile imgAvartar,
//             @RequestParam("imgBackgrourd") MultipartFile imgBackgrourd) throws IOException, java.io.IOException {
//         // logger.info("Received POST request to /user/registerSeller/uploadImg/{}", id);
//         Optional<Account> account = accountRepository.findById(id);
//         if (account.isPresent()) {
//             String avatar = UploadImage.uploadFile("avatar", imgAvartar);
//             String background = UploadImage.uploadFile("background", imgBackgrourd);
//             account.get().setAvatar(avatar);
//             account.get().setBackground(background);
//             accountRepository.save(account.get());
//             Map<String, Object> map = new HashMap<>();
//             map.put("data", account);
//             map.put("message", "Images uploaded successfully.");
//             map.put("status", "success");
//             return map;
//         } else {
//             // logger.error("Account with id {} not found", id);
//             Map<String, Object> errorMap = new HashMap<>();
//             errorMap.put("message", "Account not found");
//             errorMap.put("status", "error");
//             return errorMap;
//         }
//     }

@PostMapping("/user/uploadAvatar/{id}")
public Map<String, Object> uploadAvatar(@PathVariable("id") Integer id,
        @RequestParam("imgAvatar") MultipartFile imgAvatar) throws IOException {
    Optional<Account> account = accountRepository.findById(id);
    if (account.isPresent()) {
        String avatar = UploadImage.uploadFile("avatar", imgAvatar);
        account.get().setAvatar(avatar);
        accountRepository.save(account.get());
        Map<String, Object> map = new HashMap<>();
        map.put("data", account);
        map.put("message", "Avatar uploaded successfully.");
        map.put("status", "success");
        return map;
    } else {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", "Account not found");
        errorMap.put("status", "error");
        return errorMap;
    }
}

@PostMapping("/user/uploadBackground/{id}")
public Map<String, Object> uploadBackground(@PathVariable("id") Integer id,
        @RequestParam("imgBackground") MultipartFile imgBackground) throws IOException {
    Optional<Account> account = accountRepository.findById(id);
    if (account.isPresent()) {
        String background = UploadImage.uploadFile("background", imgBackground);
        account.get().setBackground(background);
        accountRepository.save(account.get());
        Map<String, Object> map = new HashMap<>();
        map.put("data", account);
        map.put("message", "Background uploaded successfully.");
        map.put("status", "success");
        return map;
    } else {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", "Account not found");
        errorMap.put("status", "error");
        return errorMap;
    }
}
}

