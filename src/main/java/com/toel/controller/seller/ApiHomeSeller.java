package com.toel.controller.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.response.Response_Home;
import com.toel.service.seller.Service_HomeSeller;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/home")
public class ApiHomeSeller {

        @Autowired
        Service_HomeSeller service_HomeSeller;

        @GetMapping("/getData")
        public ApiResponse<Response_Home> getHome(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
                        @RequestParam(value = "year", defaultValue = "0") Integer year) {
                return ApiResponse.<Response_Home>build()
                                .result(Response_Home.builder()
                                                .luotYeuThich(service_HomeSeller.getLike(account_id))
                                                .donChoDuyet(service_HomeSeller.getDonChoDuyet(account_id))
                                                .doanhSo(service_HomeSeller.getDoanhSo(account_id))
                                                .doanhThu(service_HomeSeller.getDoanhThu(account_id))
                                                .listDoanhSo(service_HomeSeller.getListDoanhSo(year, account_id))
                                                .listDoanhThu(service_HomeSeller.getListDoanhThu(year, account_id))
                                                .build());
        }

        @GetMapping("/getYears")
        public ApiResponse<?> getYears(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id) {
                return ApiResponse.build()
                                .result(service_HomeSeller.getYears(account_id));
        }
}
