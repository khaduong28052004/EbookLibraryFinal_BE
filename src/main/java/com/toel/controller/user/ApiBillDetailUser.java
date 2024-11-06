package com.toel.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.service.user.Service_BillDetail_User;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user/billdetail")
public class ApiBillDetailUser {

	@Autowired
	private Service_BillDetail_User service_BillDetail_User;

	/* Lấy thông tin order */
	@PostMapping("/read/{billId}")
	public ResponseEntity<Map<String, Object>> getAllOrdersByOrderStatus(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = service_BillDetail_User.getBillDetail(billId);
		return ResponseEntity.ok(response);
	}

	/* Hủy order */
	@PostMapping("/update_status/cancel/{billId}")
	public ResponseEntity<Map<String, Object>> cancelOrderByBill(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = service_BillDetail_User.cancelBill(billId);
		return ResponseEntity.ok(response);
	}

	/* Xác nhận order đã nhận */
	@PostMapping("/update_status/confirm/{billId}")
	public ResponseEntity<Map<String, Object>> confirmReceivedOrder(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = service_BillDetail_User.confirmBill(billId);
		return ResponseEntity.ok(response);
	}

	/* Mua lại order */
	@PostMapping("/create/reorder/{billId}")
	public ResponseEntity<Map<String, Object>> reOrder(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = service_BillDetail_User.reOrder(billId);
		return ResponseEntity.ok(response);
	}

}