package com.toel.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.toel.model.Account;
import com.toel.repository.AccountRepository;
 
import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class ApiUpdateAccount {
	@Autowired
	private AccountRepository accountRepository;

	public ApiUpdateAccount(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@PutMapping("/user/updateAccount/{id}")
	public ResponseEntity<Map<String, Object>> updateAccount(@PathVariable("id") Integer id,
			@RequestBody Account entity) {
		Map<String, Object> response = new HashMap<>();

		// Tìm tài khoản theo ID hoặc ném ra lỗi nếu không tìm thấy
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

		// Cập nhật các trường thông tin từ entity vào tài khoản
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
public ResponseEntity<Account> getAccountById(@PathVariable Integer id) {
    // Tìm và trả về tài khoản theo ID, ném lỗi nếu không tìm thấy
    Account account = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
    return ResponseEntity.ok(account);
}

}

