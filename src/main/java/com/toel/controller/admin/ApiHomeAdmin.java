package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.Home.Response_Home;
import com.toel.service.admin.Service_Home;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/home")
public class ApiHomeAdmin {
    @Autowired
    Service_Home service_Home;

    @GetMapping
    public ApiResponse<Response_Home> getHome() {
        return ApiResponse.<Response_Home>build()
                .result(Response_Home.builder()
                        .sumShop(service_Home.getCountByRole(3))
                        .sumAccount(service_Home.getCountByRole(4))
                        .listAccount(service_Home.getCharAccount())
                        .listDoanhThu(service_Home.getCharttDoanhThu())
                        .build());
    }

}
