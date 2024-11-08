package com.toel.controller.seller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.Role;
import com.toel.model.Account;
import com.toel.model.RolePermission;
import com.toel.repository.AccountRepository;
import com.toel.repository.RolePermissionRepository;
import com.toel.repository.RoleRepository;
import com.toel.service.firebase.UploadImage;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1")
public class ApiRegisterSeller {
    private static final Logger logger = LoggerFactory.getLogger(ApiRegisterSeller.class);

    @Autowired
    AccountRepository accountRepositoty;
    @Autowired
    UploadImage firebaseStorageService;
    @Autowired
    RolePermissionRepository roleDetailRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;
    @PutMapping("/user/registerSeller/{id}")
    public ResponseEntity<Map<String, Object>> registerSeller(@PathVariable("id") Integer id,
            @RequestBody Account entity) {
        logger.info("Received PUT request to /registerSeller/{}", id);
        
        Optional<Account> optionalAccount = accountRepositoty.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            // Cập nhật thông tin tài khoản
            account.setNumberId((entity.getNumberId()));
            account.setShopName(entity.getShopName());
            account.setPhone(entity.getPhone());

            // Lưu thay đổi
            accountRepositoty.save(account);

            // Chuẩn bị phản hồi thành công
            Map<String, Object> response = new HashMap<>();
            response.put("data", account);
            response.put("message", "Successfully updated seller information");
            response.put("status", "success");
            
            return ResponseEntity.ok(response); // Trả về HTTP 200 với nội dung JSON
        } else {
            logger.error("Account with id {} not found", id);

            // Chuẩn bị phản hồi lỗi
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Account not found");
            errorResponse.put("status", "error");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // Trả về HTTP 404 với nội dung lỗi
        }
    }

    @PostMapping("/user/registerSeller/uploadImg/{id}")
    public Map<String, Object> updateRegister(@PathVariable("id") Integer id,
            @RequestParam("imgBefore") MultipartFile imgbefore,
            @RequestParam("imgAfter") MultipartFile imgafter) throws IOException {
        logger.info("Received POST request to /user/registerSeller/uploadImg/{}", id);
        Optional<Account> account = accountRepositoty.findById(id);

        if (account.isPresent()) {
            String before = firebaseStorageService.uploadFile("carid", imgbefore);
            String after = firebaseStorageService.uploadFile("carid", imgafter);

            account.get().setBeforeIdImage(before);
            account.get().setAfterIdImage(after);
            accountRepositoty.save(account.get());

            Map<String, Object> map = new HashMap<>();
            map.put("data", account);
            map.put("message", "Images uploaded successfully.");
            map.put("status", "success");
            return map;
        } else {
            logger.error("Account with id {} not found", id);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("message", "Account not found");
            errorMap.put("status", "error");
            return errorMap;
        }
    }

    @GetMapping("/user/registerSeller/{id}")
    public Map<String, Object> getRegister(@PathVariable("id") Integer Id) {
        Optional<Account> account = accountRepository.findById(Id);
String seller = "Đang chờ duyệt";
boolean status = false;

if (account.isPresent()) {
    Account accountDetail = account.get();
    
    // Assuming getRole() returns a single RolePermission, not a collection
    if ("SELLER".equalsIgnoreCase(accountDetail.getRole().getName())) {
        seller = "Đã duyệt";
    }
    
    // Check if number ID is not empty
    // if (!accountDetail.getNumberId().isEmpty()) {
    //     status = true;
    // }
}

        Map<String, Object> map = new HashMap<>();
        map.put("seller", seller);
        map.put("status", status);
        map.put("shopName", account.get().getShopName());
        map.put("phone", account.get().getPhone());
        map.put("numberId", account.get().getNumberId());
        return map;
    }

    // CHECK TÀI KHOẢN ĐÃ ĐĂNG KÝ
    @GetMapping("/user/registerSeller/check-cccd/{cccdNumber}")
    public ResponseEntity<Map<String, Boolean>> checkCCCDExists(@PathVariable String cccdNumber) {
        // Log số CCCD để kiểm tra
        System.out.println("Checking CCCD: " + cccdNumber); 

        boolean exists = accountRepository.existsByNumberId(cccdNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        // Log phản hồi để kiểm tra
        System.out.println("CCCD exists: " + exists);
        
        return ResponseEntity.ok(response);
    }
    // hehe
}