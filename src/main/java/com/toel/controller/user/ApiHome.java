package com.toel.controller.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.repository.AccountRepository;
import com.toel.repository.FlashSaleDetailRepository;
import com.toel.repository.FlashSaleRepository;
import com.toel.service.user.Service_SelectAllProductHome;
import com.toel.service.user.Service_SelectFlashSale;

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
			@RequestParam(name = "sort", defaultValue = "price") String sort) {
		List<FlashSaleDetail> flashSaleDetails = new ArrayList<FlashSaleDetail>();
		LocalDateTime localDateTime = LocalDateTime.now();
		FlashSale flashSale = flashSaleRepo.findFlashSaleNow(localDateTime);
		try {

			flashSaleDetails = flashSaleDetailRepo.findAllByFlashSale(flashSale);
		} catch (Exception e) {
			// TODO: handle exception
		}

		Map<String, Object> response = serviceSellectAll.selectAll(flashSaleDetails, id_Shop, 0, size, sort);
		response.put("flashSalene", flashSale);
		if (response.get("error") != null) {
			return ApiResponse.<Map<String, Object>>build().message("not fault").code(1002);
		}

		return ApiResponse.<Map<String, Object>>build().message("success").result(response);
	}

}
