package com.toel.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.service.UserProductActionsService;
import com.toel.service.user.Service_ProductDetail;
import com.toel.service.user.Service_RelatedProduct;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/user/productdetail")
public class ApiProductDetail {
	@Autowired
	Service_ProductDetail service_ProductDetail;
	@Autowired
	Service_RelatedProduct service_RelatedProduct;
	@Autowired
    UserProductActionsService userProductActionsService;
	@GetMapping("product/{idProduct}")
	public ApiResponse<Map<String, Object>> productDetail(@PathVariable("idProduct") Integer id_Product) {
		try {
			userProductActionsService.handleUserAction(null, id_Product, "VIEW");
			return ApiResponse.<Map<String, Object>>build().message("success")
					.result(service_ProductDetail.getProduct(id_Product));
		} catch (Exception e) {
			return ApiResponse.<Map<String, Object>>build().message("get productdetail error").code(1002);
		}

	}

	@GetMapping("related")
	public ApiResponse<Map<String, Object>> getRelatedProduct(
			@RequestParam(name = "idUser", defaultValue = "0") Integer id_User,
			@RequestParam(name = "size", defaultValue = "8") Integer size,
			@RequestParam(name = "sort", defaultValue = "price") String sort) {
		try {
			return ApiResponse.<Map<String, Object>>build().message("success")
					.result(service_ProductDetail.getAllRelated(id_User, size, sort));
		} catch (Exception e) {
			return ApiResponse.<Map<String, Object>>build().message("get productdetail error").code(1002);
		}
	}

	@GetMapping("seller")
	public ApiResponse<Map<String, Object>> getProductSeller(
			@RequestParam(name = "size", defaultValue = "8") Integer size,
			@RequestParam(name = "sort", defaultValue = "price") String sort) {
		try {
			return ApiResponse.<Map<String, Object>>build().message("success")
					.result(service_ProductDetail.getAllProduct(size, sort));
		} catch (Exception e) {
			return ApiResponse.<Map<String, Object>>build().message("get productdetail error").code(1002);
		}
	}
}
