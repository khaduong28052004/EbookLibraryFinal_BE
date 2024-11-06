package com.toel.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.user.resquest.Request_Bill_User;
import com.toel.service.user.Service_Bill_User;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user/bill")
public class ApiBillUser {

	@Autowired
	private Service_Bill_User billService_Bill;

	/* Lấy thông tin order */
	@PostMapping("/read")
	public ResponseEntity<Map<String, Object>> getAllOrdersByOrderStatus(
			@RequestBody Request_Bill_User requestBillDTO) {
		Map<String, Object> response = billService_Bill.getBills(requestBillDTO);
		return ResponseEntity.ok(response);
	}

	/* Hủy order */
	@PostMapping("/update_status/cancel/{billId}")
	public ResponseEntity<Map<String, Object>> cancelOrderByBill(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = billService_Bill.cancelBill(billId);
		return ResponseEntity.ok(response);
	}

	/* Xác nhận order đã nhận */
	@PostMapping("/update_status/confirm/{billId}")
	public ResponseEntity<Map<String, Object>> confirmReceivedOrder(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = billService_Bill.confirmBill(billId);
		return ResponseEntity.ok(response);
	}

	/* Mua lại order */
	@PostMapping("/create/reorder/{billId}")
	public ResponseEntity<Map<String, Object>> reOrder(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = billService_Bill.reOrder(billId);
		return ResponseEntity.ok(response);
	}

}