package com.toel.controller.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.user.response.Response_BillDetail_User;
import com.toel.dto.user.response.Response_Bill_User;
import com.toel.dto.user.resquest.Request_Bill_User;
import com.toel.model.Bill;
import com.toel.model.Evalue;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.CartRepository;
import com.toel.repository.EvalueRepository;
import com.toel.repository.OrderStatusRepository;
import com.toel.repository.ProductReportRepository;
import com.toel.repository.config.admin.Config_Repo_Account;
import com.toel.repository.config.user.Config_Repo_CartDetail;
import com.toel.service.user.Service_BillDetail_User;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/billdetail")
public class ApiBillDetailUser {

	@Autowired
	private Service_BillDetail_User service_BillDetail_User;

	/* Lấy thông tin order */
	@PostMapping("/read/{billId}")
	public ResponseEntity<Map<String, Object>> getAllOrdersByOrderStatus(@PathVariable("billId") Integer billId) {
		Map<String, Object> response = service_BillDetail_User.getBillDetail(billId);
		return ResponseEntity.ok(response);
	}

//	/* Hủy order */
//	@PostMapping("/update_status/cancel/{billId}")
//	public ResponseEntity<Map<String, Object>> cancelOrderByBill(@PathVariable("billId") Integer billId) {
//		Map<String, Object> response = billService_Bill.cancelBill(billId);
//		return ResponseEntity.ok(response);
//	}
//
//	/* Xác nhận order đã nhận */
//	@PostMapping("/update_status/confirm/{billId}")
//	public ResponseEntity<Map<String, Object>> confirmReceivedOrder(@PathVariable("billId") Integer billId) {
//		Map<String, Object> response = billService_Bill.confirmBill(billId);
//		return ResponseEntity.ok(response);
//	}
//
//	/* Mua lại order */
//	@PostMapping("/create/reorder/{billId}")
//	public ResponseEntity<Map<String, Object>> reOrder(@PathVariable("billId") Integer billId) {
//		Map<String, Object> response = billService_Bill.reOrder(billId);
//		return ResponseEntity.ok(response);
//	}

}