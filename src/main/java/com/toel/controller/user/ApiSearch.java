package com.toel.controller.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.service.user.Service_Search;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/user")
public class ApiSearch {

	@Autowired
	Service_Search service_Search;

	@GetMapping("search")
	public ApiResponse<Map<String, Object>> search(@RequestParam("text") String text) {
		return ApiResponse.<Map<String, Object>>build().message("success")
				.result(service_Search.getProductByName(text, 10, "createAt"));
	}

	@GetMapping("filtercategory")
	public ApiResponse<Map<String, Object>> filtercategory(@RequestParam("id_categories") List<Integer> id_categories) {
		return ApiResponse.<Map<String, Object>>build().message("success")
				.result(service_Search.filterProductByCategory(id_categories, 10, "createAt"));
	}

	@GetMapping("filterprice")
	public ApiResponse<Map<String, Object>> filterPrice(@RequestParam("priceMin") double priceMin,
			@RequestParam("priceMax") double priceMax) {
		return ApiResponse.<Map<String, Object>>build().message("success")
				.result(service_Search.filterProductByPrice(priceMin, priceMax, 10, "createAt"));
	}
}
