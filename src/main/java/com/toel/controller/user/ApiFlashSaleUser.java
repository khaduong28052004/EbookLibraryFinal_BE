package com.toel.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.model.FlashSale;
import com.toel.service.user.FlashSaleService;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/user/flashsale")
public class ApiFlashSaleUser {
	@Autowired
	FlashSaleService flashSaleService;

	@GetMapping("getall")
	public ApiResponse<List<FlashSale>> getAllFlashSaleByNow() {
		try {
			List<FlashSale> listFlashSales = flashSaleService.getAllFlashSaleByNow();
			if (!listFlashSales.isEmpty()) {
				return ApiResponse.<List<FlashSale>>build().message("get all flashsale now success.")
						.result(listFlashSales);
			} else {
				return ApiResponse.<List<FlashSale>>build().message("get all flashsale now error.");
			}
		} catch (Exception e) {
			System.out.println("get all flashsale error " + e);
			return ApiResponse.<List<FlashSale>>build().message("get all flashsale now error : " + e);
		}
	}

	@GetMapping("flashsaledetail/getallby/{id}")
	public ApiResponse<List<Response_FlashSaleDetail>> getAllFlashSaleDetailByFlashSale(
			@PathVariable("id") Integer id_flashsale) {
		try {
			List<Response_FlashSaleDetail> listResponse_FlashSaleDetails = flashSaleService
					.getAllFlashSaleDetailByFlashSale(id_flashsale);
			if (!listResponse_FlashSaleDetails.isEmpty()) {
				return ApiResponse.<List<Response_FlashSaleDetail>>build()
						.message("get all flashsaledetail by flashsale success.").result(listResponse_FlashSaleDetails);
			} else {
				return ApiResponse.<List<Response_FlashSaleDetail>>build().code(1002)
						.message("get all flashsaledetail by flashsale error.");
			}
		} catch (Exception e) {
			return ApiResponse.<List<Response_FlashSaleDetail>>build().code(1002)
					.message("get all flashsaledetail by flashsale error : " + e);
		}
	}
}
