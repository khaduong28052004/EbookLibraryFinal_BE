package com.toel.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

}