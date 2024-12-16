package com.toel.controller.user;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.dto.user.response.Response_Product;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.repository.AccountRepository;
import com.toel.repository.FlashSaleDetailRepository;
import com.toel.repository.FlashSaleRepository;
import com.toel.service.user.Service_SelectAllProductHome;
import com.toel.service.user.Service_SelectFlashSale;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/v1/user/home")
@CrossOrigin("*")
public class ApiHome {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	FlashSaleRepository flashSaleRepo;
	@Autowired
	Service_SelectFlashSale service_SelectFlashSale;
	@Autowired
	Service_SelectAllProductHome serviceSellectAll;
	@Autowired
	FlashSaleDetailRepository flashSaleDetailRepo;

	@RequestMapping("flashsale")
	public ApiResponse<Map<String, Object>> selectFlashSale(
			@RequestParam(name = "id_Shop", defaultValue = "0") Integer id_Shop) {
		Map<String, Object> map = new HashMap<>();
		LocalDateTime localDateTime = LocalDateTime.now();
		// Date localDateTime = new Date();
		// SimpleDateFormat dp = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		// dp.format(localDateTime);

		try {
			FlashSale flashSale = flashSaleRepo.findFlashSaleNow(localDateTime);
			List<Response_FlashSaleDetail> response = service_SelectFlashSale.selectFlashSale(flashSale, id_Shop);
			map.put("datas", response);
			map.put("lastDate", flashSale.getDateEnd());
			return ApiResponse.<Map<String, Object>>build().message("data success").result(map);

		} catch (Exception e) {
			return ApiResponse.<Map<String, Object>>build().message("not fault").code(1002);
		}

	}

	@RequestMapping("selectall")
	public ApiResponse<Map<String, Object>> selectAll(
			@RequestParam(name = "id_Shop", defaultValue = "0") Integer id_Shop,
			@RequestParam(name = "size", defaultValue = "8") Integer size,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "sort", defaultValue = "price") String sort) {
		List<FlashSaleDetail> flashSaleDetails = new ArrayList<FlashSaleDetail>();
		LocalDateTime localDateTime = LocalDateTime.now();
		FlashSale flashSale = flashSaleRepo.findFlashSaleNow(localDateTime);
		try {

			flashSaleDetails = flashSaleDetailRepo.findAllByFlashSale(flashSale);
		} catch (Exception e) {
		}

		Map<String, Object> response = serviceSellectAll.selectAll(flashSaleDetails, id_Shop, page, size, sort);
		// response.put("flashSalene", flashSale);
		if (response.get("error") != null) {
			return ApiResponse.<Map<String, Object>>build().message("not fault").code(1002);
		}

		return ApiResponse.<Map<String, Object>>build().message("success").result(response);
	}

	// suggest
	@GetMapping("suggest")
	public ApiResponse<List<Response_Product>> suggests(
			@RequestParam(name = "id_user", defaultValue = "6") Integer id_user) {
		return ApiResponse.<List<Response_Product>>build().message("fetch suggest success")
				.result(serviceSellectAll.suggestProduct(id_user));
	}

	//// suggest
	// @GetMapping("suggest")
	// public ApiResponse<Integer> suggests(
	// @RequestParam(name = "id_user", defaultValue = "6") Integer id_user) {
	//// return ApiResponse.<List<Response_Product>>build().message("fetch suggest
	//// success")
	//// .result(serviceSellectAll.suggestProduct(id_user));
	// return ApiResponse.<Integer>build().message("fetch suggest success")
	// .result(id_user);
	// }

}
