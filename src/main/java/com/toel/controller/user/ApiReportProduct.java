package com.toel.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.user.resquest.Request_ProductReport;
import com.toel.service.user.Service_ReportProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("api/v1/user/report")
public class ApiReportProduct {
	@Autowired
	Service_ReportProduct service_ReportProduct;

	@PostMapping("product")
	public ApiResponse<Boolean> createReport(@ModelAttribute Request_ProductReport report) {
		return ApiResponse.<Boolean>build().result(service_ReportProduct.report(report));
	}

	@GetMapping("checkReport")
	public ApiResponse<Boolean> checkReport(@RequestParam("id_user") Integer id_user,
			@RequestParam("id_product") Integer id_product) {
		return ApiResponse.<Boolean>build().result(service_ReportProduct.checkReport(id_user, id_product));
	}

}
