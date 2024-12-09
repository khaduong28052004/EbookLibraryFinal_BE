package com.toel.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.user.resquest.Request_Evaluate_User;
import com.toel.service.user.Service_Evaluate_User;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user/evaluate")
public class ApiEvaluateUser {
	@Autowired
	private Service_Evaluate_User serviceEvaluate_User;

	@PostMapping("/create/saveImg")
	public ResponseEntity<Map<String, Object>> getAllOrdersByOrderStatus(
			@Valid @ModelAttribute Request_Evaluate_User requestEvaluateDTO, BindingResult bindingResult) {
		Map<String, Object> response = serviceEvaluate_User.saveEvaluate(requestEvaluateDTO);
		return ResponseEntity.ok(response);
	}

}