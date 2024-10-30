package com.toel.controller.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.request.Request_Account;
import com.toel.dto.seller.response.Response_Account;
import com.toel.service.seller.Service_Shop;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/shop")
public class ApiShop {
    @Autowired
    Service_Shop service_Shop;

    @GetMapping("/get")
    public ApiResponse<Response_Account> get(
            @RequestParam(value = "account_id", defaultValue = "0") Integer account_id) {
        return ApiResponse.<Response_Account>build()
                .result(service_Shop.get(account_id));
    }

    @PostMapping("/save")
    public ApiResponse<Response_Account> save(
            @RequestBody @Valid Request_Account request_Account) {
        return ApiResponse.<Response_Account>build()
                .message("Cập nhật thông tin Shop thành công")
                .result(service_Shop.save(request_Account));
    }
}
