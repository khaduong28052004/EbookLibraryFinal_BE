package com.toel.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping("cart/add")
	public ApiResponse<Map<String, Object>> addCart(@RequestParam("id_user") Integer id_user,
			@RequestParam("id_product") Integer product, @RequestParam("quantity") Integer quantity) {
		return ApiResponse.<Map<String, Object>>build().message("success")
				.result(service_Cart.addCart(id_user, quantity, product));
	}

	@RequestMapping("cart/remove/{id}")
	public ApiResponse<Map<String, Object>> removeCart(@PathVariable("id") Integer id_cart) {
		if (service_Cart.removeCart(id_cart) != null) {
			return ApiResponse.<Map<String, Object>>build().message("success").result(service_Cart.removeCart(id_cart));
		} else {
			return ApiResponse.<Map<String, Object>>build().message("success").result(service_Cart.removeCart(id_cart))
					.code(1002);
		}
	}

}
