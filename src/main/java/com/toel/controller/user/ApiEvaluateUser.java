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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.user.response.Response_Bill_User;
import com.toel.dto.user.resquest.Request_Bill_User;
import com.toel.dto.user.resquest.Request_Evaluate_User;
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
import com.toel.service.user.Service_Bill_User;
import com.toel.service.user.Service_Evaluate_User;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/evaluate")
public class ApiEvaluateUser {

	@Autowired
	private Service_Evaluate_User serviceEvaluate_User;

	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> getAllOrdersByOrderStatus(
			@Valid @ModelAttribute Request_Evaluate_User requestEvaluateDTO, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			Map<String, Object> response = new HashMap<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				response.put(error.getField(), error.getDefaultMessage());
				response.put("status", "fail");
			}
			return ResponseEntity.badRequest().body(response);
		}
		Map<String, Object> response = serviceEvaluate_User.saveEvaluate(requestEvaluateDTO);
		return ResponseEntity.ok(response);
	}

	/* Hủy order */
//	@PostMapping("/update_status/cancel/{billId}")
//	public ResponseEntity<Map<String, Object>> cancelOrderByBill(@PathVariable("billId") Integer billId) {
//		Map<String, Object> response = serEvaluate_User.saveEvaluate(billId);
//		return ResponseEntity.ok(response);
//	}

	/* Xác nhận order đã nhận */
//	@PostMapping("/update_status/confirm/{billId}")
//	public ResponseEntity<Map<String, Object>> confirmReceivedOrder(@PathVariable("billId") Integer billId) {
//		Map<String, Object> response = billService_Bill.confirmBill(billId);
//		return ResponseEntity.ok(response);
//	}

	/* Mua lại order */
//	@PostMapping("/create/reorder/{billId}")
//	public ResponseEntity<Map<String, Object>> reOrder(@PathVariable("billId") Integer billId) {
//		Map<String, Object> response = billService_Bill.reOrder(billId);
//		return ResponseEntity.ok(response);
//	}

}