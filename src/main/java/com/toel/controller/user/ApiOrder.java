package com.toel.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.toel.repository.config.admin.Config_Repo_Account;
import com.toel.repository.config.user.Config_Repo_CartDetail;
import com.toel.repository.config.user.Config_Repo_Evaluate;
import com.toel.repository.config.user.Config_Repo_Order;
import com.toel.repository.config.user.Config_Repo_OrderDetail;
import com.toel.repository.config.user.Config_Repo_OrderStatus;
import com.toel.repository.config.user.Config_Repo_Product;

public class ApiOrder {
	@Autowired
	Config_Repo_Order orderRepository;
	@Autowired
	Config_Repo_OrderDetail orderDetailRepository;
	@Autowired
	Config_Repo_OrderStatus orderStatusRepository;
	@Autowired
	Config_Repo_Account accountRepository;
	@Autowired
	Config_Repo_Product productRepository;
	@Autowired
	Config_Repo_Evaluate evalueRepository;
	@Autowired
	Config_Repo_CartDetail cartDetailRepository;

	/* Lấy thông tin order */
	public ResponseEntity<Map<String, Object>> getAllOrdersByUser(@PathVariable("id") Integer id,
			@RequestParam String orderStatusFind) {
		Map<String, Object> response = new HashMap<String, Object>();
		return ResponseEntity.ok(response);
	}

	/* Hủy order */
	public ResponseEntity<Map<String, Object>> cancelOrderByBill(@PathVariable("id") Integer id,
			@RequestParam String orderStatusFind) {
		Map<String, Object> response = new HashMap<String, Object>();
		return ResponseEntity.ok(response);

	}

	/* Xác nhận order đã nhận */
	public ResponseEntity<Map<String, Object>> confirmReceivedOrder(@PathVariable("id") Integer id,
			@RequestParam String orderStatusFind) {
		Map<String, Object> response = new HashMap<String, Object>();
		return ResponseEntity.ok(response);
	}

	/* Mua lại order */
	public ResponseEntity<Map<String, Object>> reOrder(@PathVariable("id") Integer id,
			@RequestParam String orderStatusFind) {
		Map<String, Object> response = new HashMap<String, Object>();
		return ResponseEntity.ok(response);
	}
}