package com.toel.controller.user;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.user.response.Response_Bill;
import com.toel.dto.user.resquest.Request_Bill;
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
import com.toel.repository.config.user.Config_Repo_Evaluate;
import com.toel.repository.config.user.Config_Repo_Order;
import com.toel.repository.config.user.Config_Repo_OrderDetail;
import com.toel.repository.config.user.Config_Repo_OrderStatus;
import com.toel.repository.config.user.Config_Repo_Product;
import com.toel.service.user.Service_Bill;

@CrossOrigin("*")
@RestController("/api/v1/bill")
public class ApiBill {
    @Autowired
    private Service_Bill billService_Bill;
    
	/* Lấy thông tin order */
    @PostMapping("/read")
    public ResponseEntity<Map<String, Object>> getAllOrdersByOrderStatus(@RequestBody Request_Bill requestBillDTO ) {
    	Map<String, Object> response = new HashMap<>();
    	try {
			List<Object[]> productsInBill = billService_Bill.getBillsByOrderStatus(requestBillDTO);
			List<Response_Bill> shopListInBill = billService_Bill.createBillsWithProductsInBillDetail(productsInBill);
			response.put("data", shopListInBill);
			response.put("status", "success");
			response.put("message", "Retrieve data successfully");
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred while retrieving orders.");
	        response.put("error", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return ResponseEntity.ok(response);	
	}

	/* Hủy order */
    @PostMapping("/update_status/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrderByBill(@PathVariable("id") Integer id,
			@RequestParam String orderStatusFind) {
		Map<String, Object> response = new HashMap<String, Object>();
		return ResponseEntity.ok(response);

	}

	/* Xác nhận order đã nhận */
    @PostMapping("/update_status/confirm")
    public ResponseEntity<Map<String, Object>> confirmReceivedOrder(@PathVariable("id") Integer id,
			@RequestParam String orderStatusFind) {
		Map<String, Object> response = new HashMap<String, Object>();
		return ResponseEntity.ok(response);
	}

	/* Mua lại order */
    @PostMapping("/create/reorder")
    public ResponseEntity<Map<String, Object>> reOrder(@PathVariable("id") Integer id,
			@RequestParam String orderStatusFind) {
		Map<String, Object> response = new HashMap<String, Object>();
		return ResponseEntity.ok(response);
	}

}