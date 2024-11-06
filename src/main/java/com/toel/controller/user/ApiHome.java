package com.toel.controller.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.dto.user.response.Response_Product;
import com.toel.repository.AccountRepository;
import com.toel.service.admin.Service_FlashSaleDetail;
import com.toel.service.user.Service_SelectAllProductHome;
import com.toel.service.user.Service_SelectFlashSale;

@RestController
@RequestMapping("api/v1/user/home")
public class ApiHome {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	Service_SelectFlashSale service_SelectFlashSale;
	Service_SelectAllProductHome serviceSellectAll;

	@RequestMapping("flashsale")
	public ApiResponse<List<Response_FlashSaleDetail>> selectFlashSale(
			@RequestParam(name = "id_Shop", defaultValue = "0") Integer id_Shop) {
		return ApiResponse.<List<Response_FlashSaleDetail>>build().message("data success")
				.result(service_SelectFlashSale.selectFlashSale(id_Shop));
	}

	@RequestMapping("sellectall")
	public ApiResponse<Map<String, Object>> selectAll(
			@RequestParam(name = "id_Shop", defaultValue = "0") Integer id_Shop, @RequestParam("size") Integer size,
			@RequestParam("sort") String sort) {
		LocalDateTime now = LocalDateTime.now();
		Map<String, Object> response = serviceSellectAll.selectAll(service_SelectFlashSale.selectFlashSale(id_Shop),
				id_Shop, 0, 8, sort);
		return ApiResponse.<Map<String, Object>>build().message("success").result(response);
	}

}
