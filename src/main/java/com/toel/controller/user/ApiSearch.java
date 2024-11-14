package com.toel.controller.user;

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
}
