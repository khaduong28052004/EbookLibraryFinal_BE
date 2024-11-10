package com.toel.controller.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.toel.dto.user.resquest.Request_Bill_User;



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
	        @RequestParam int userId) {
		
		   Request_Bill_User requestBillDTO = new Request_Bill_User();
		    requestBillDTO.setOrderStatusFind(orderStatusFind);
		    requestBillDTO.setUserID(userId);
		    
		Map<String, Object> response = service_Bill_User.getBills(requestBillDTO);

		return ResponseEntity.ok(response);
	}

	/* Hủy order */
	@PostMapping("/update_status/cancel/{billId}")
	public ResponseEntity<Map<String, Object>> cancelOrderByBill(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = new HashMap<>();
		try {
			service_Bill_User.cancelBill(billId);
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
	


}