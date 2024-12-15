package com.toel.controller.user;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.user.resquest.Request_Bill_User;
import com.toel.model.Bill;
import com.toel.repository.BillRepository;
import com.toel.service.Email.EmailService;
import com.toel.service.user.Service_Bill_User;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user/bill")
public class ApiBillUser {

	@Autowired
	private Service_Bill_User service_Bill_User;

	/* Lấy thông tin order */
	@GetMapping("/read")
	public ResponseEntity<Map<String, Object>> getAllOrdersByOrderStatus(
			@RequestParam String orderStatusFind,
			@RequestParam int userId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size) {

		Request_Bill_User requestBillDTO = new Request_Bill_User();
		requestBillDTO.setOrderStatusFind(orderStatusFind);
		requestBillDTO.setUserID(userId);
		requestBillDTO.setPage(page);
		requestBillDTO.setSize(size);
		Map<String, Object> response = service_Bill_User.getBills(requestBillDTO);
		return ResponseEntity.ok(response);
	}

	/* Hủy order */
	@PostMapping("/update_status/cancel/{billId}")
	public ResponseEntity<Map<String, Object>> cancelOrderByBill(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = new HashMap<>();
		try {

			service_Bill_User.cancelBill(billId);

			// Bill bill = billRepository.findById(billId).get();
			// Account user = bill.getAccount();
			// if (service_Bill_User.checkAndBlockUsers(user.getId()) == 2) {
			// response.put("checkCancel",
			// "Bạn đã hủy đơn quá nhiều lần trong hôm nay. Nếu tiếp tục, tài khoản của bạn
			// có thế bị khóa");
			// }

			response.put("message", "Hủy đơn thành công");
			response.put("status", "successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("message", e.getMessage());
			response.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	/* Xác nhận order đã nhận */
	@PostMapping("/update_status/confirm/{billId}")
	public ResponseEntity<Map<String, Object>> confirmReceivedOrder(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = new HashMap<>();
		try {
			service_Bill_User.confirmBill(billId);
			response.put("message", "Xác nhận đơn hàng thành công");
			response.put("status", "successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("message", e.getMessage());
			response.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/test_auto_confirm")
	public ResponseEntity<Map<String, Object>> autoConfirmOrders() {
		Map<String, Object> response = new HashMap<>();
		try {
			service_Bill_User.autoConfirmOrders();
			response.put("message", "Đơn hàng đã được xác nhận tự động sau 7 ngày giao hàng");
			response.put("status", "success");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("message", e.getMessage());
			response.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	/* Mua lại order */
	@PostMapping("/create/reorder/{billId}")
	public ResponseEntity<Map<String, Object>> reOrder(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = new HashMap<>();
		try {

			service_Bill_User.reOrder(billId);

			response.put("message", "Đã thêm vào giỏ hàng");
			response.put("status", "successfully");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("message", e.getMessage());
			response.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@Autowired
	BillRepository billRepository;
	@Autowired
	EmailService emailService;

	@PostMapping("/send")
	public ResponseEntity<String> sendNotification(@RequestParam Integer billId) {
		Optional<Bill> optionalBill = billRepository.findById(billId);
		if (optionalBill.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Bill with ID " + billId + " not found.");
		}

		Bill bill = optionalBill.get();

		// Call the sendNotification method
		String email = bill.getAccount().getEmail();
		String username = bill.getAccount().getUsername();

		String subject = "TOEL - Thông báo cập nhật trạng thái xác nhận đơn hàng";
		String content = String.format(
				"Dear %s,\n\nĐơn hàng của bạn sẽ được tự động cập nhật trạng thái sau 2 ngày. "
						+ "Vui lòng xác nhận trạng thái đã nhận hàng.\n\nXin cảm ơn vì đã mua hàng trên TOEL.",
				username);

		emailService.push(email, subject, content);

		return ResponseEntity.ok("Notification sent to " + email);
	}

}