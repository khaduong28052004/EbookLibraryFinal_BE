package com.toel.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.service.user.Service_BillDetail_User;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user/billdetail")
public class ApiBillDetailUser {

	@Autowired
	private Service_BillDetail_User service_BillDetail_User;

	/* Lấy thông tin order */
	@GetMapping("/read")
	public ResponseEntity<Map<String, Object>> getAllOrdersByOrderStatus(@RequestParam("billId") Integer billId) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			Map<String, Object> shopListInBill = service_BillDetail_User.getBillDetail(billId);
			response.put("data", shopListInBill);
			response.put("status", "success");
			response.put("message", "Retrieve data successfully");
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "An error occurred while retrieving orders.");
			response.put("error", e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	/* Hủy order */
	@PostMapping("/update_status/cancel/{billId}")
	public ResponseEntity<Map<String, Object>> cancelOrderByBill(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = new HashMap<>();
		try {
			service_BillDetail_User.cancelBill(billId);
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
			service_BillDetail_User.confirmBill(billId);
			response.put("message", "Xác nhận đơn hàng thành công");
			response.put("status", "successfully");
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

			service_BillDetail_User.reOrder(billId);
			response.put("message", "Đã thêm vào giỏ hàng");
			response.put("status", "successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("message", e.getMessage());
			response.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}