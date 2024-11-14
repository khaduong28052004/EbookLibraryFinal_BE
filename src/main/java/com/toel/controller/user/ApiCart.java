package com.toel.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.service.user.Service_Cart;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/user")
public class ApiCart {
	@Autowired
	Service_Cart service_Cart;

	@RequestMapping("cart/{id}")
	public ApiResponse<Map<String, Object>> getAllCart(@PathVariable("id") Integer id_User) {
		return ApiResponse.<Map<String, Object>>build().message("success").result(service_Cart.getCart(id_User));
	}
}
