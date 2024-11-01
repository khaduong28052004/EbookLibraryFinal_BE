package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.Account.Request_AccountCreate;
import com.toel.dto.admin.request.FlashSale.Request_FlashSaleUpdate;
import com.toel.dto.admin.response.Response_Account;
import com.toel.dto.admin.response.Response_FlashSale;
import com.toel.service.admin.Service_Account;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/account")
public class ApiAccountAdmin {
    @Autowired
    Service_Account service_Account;

    @GetMapping
    public ApiResponse<PageImpl<Response_Account>> getAllNhanVApiResponse(
            @RequestParam(value = "role", required = false) Boolean role,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "gender", required = false) Boolean gender,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        PageImpl<Response_Account> pageImpl;
        if (Boolean.TRUE) {
            pageImpl = service_Account.getAll("ADMINV1", search, gender, page, size, sortBy, sortColumn);
        } else if (Boolean.FALSE) {
            pageImpl = service_Account.getAll("USER", search, gender, page, size, sortBy, sortColumn);
        } else {
            pageImpl = null;
        }
        return ApiResponse.<PageImpl<Response_Account>>build()
                .result(pageImpl);
    }

    @PostMapping("nhanvien")
    public ApiResponse<Response_Account> post(@RequestBody @Valid Request_AccountCreate entity) {
        return ApiResponse.<Response_Account>build()
                .result(service_Account.create("ADMINV1", entity));
    }

    @PutMapping
    public ApiResponse<Response_Account> put(@RequestParam(value = "id", required = false) Integer id) {
        return ApiResponse.<Response_Account>build()
                .result(service_Account.updateStatus(id));
    }

    @DeleteMapping
    public ApiResponse<Response_Account> delete(
            @RequestParam(value = "id", required = false) Integer id) {
        service_Account.delete(id);
        return ApiResponse.<Response_Account>build()
                .message("Xóa nhân viên thành công");
    }

}