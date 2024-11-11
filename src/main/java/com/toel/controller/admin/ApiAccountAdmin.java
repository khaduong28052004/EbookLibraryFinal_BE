package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.Account.Request_AccountCreate;
import com.toel.dto.admin.response.Response_Account;
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
    public ApiResponse<PageImpl<?>> getAll(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "gender", required = false) Boolean gender,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        PageImpl<?> pageImpl;
        if (role.equalsIgnoreCase("adminv1")) {
            pageImpl = service_Account.getAll("ADMINV1", search, gender, page, size, sortBy, sortColumn);
        } else if (role.equalsIgnoreCase("user")) {
            pageImpl = service_Account.getAll("USER", search, gender, page, size, sortBy, sortColumn);
        } else if (role.equalsIgnoreCase("seller")) {
            pageImpl = service_Account.getAll("SELLER", search, gender, page, size, sortBy, sortColumn);
        } else {
            pageImpl = null;
        }
        return ApiResponse.<PageImpl<?>>build()
                .result(pageImpl);
    }

    @GetMapping("seller/browse")
    public ApiResponse<PageImpl<Response_Account>> getAllSellerBrowse(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "gender", required = false) Boolean gender,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_Account>>build()
                .result(service_Account.getAllSellerNotBorwse(search, gender, page, size, sortBy, sortColumn));
    }

    @PostMapping("adminv1")
    public ApiResponse<Response_Account> post(@RequestBody @Valid Request_AccountCreate entity) {
        return ApiResponse.<Response_Account>build()
                .result(service_Account.create("ADMINV1", entity));
    }

    @PutMapping
    public ApiResponse<Response_Account> putStatus(@RequestParam(value = "id", required = false) Integer id) {
        return ApiResponse.<Response_Account>build()
                .result(service_Account.updateStatus(id, null));
    }

    @PutMapping("seller/browse")
    public ApiResponse<Response_Account> putActive(@RequestParam(value = "id", required = false) Integer id) {
        return ApiResponse.<Response_Account>build()
                .result(service_Account.updateActive(id));
    }

    @DeleteMapping
    public ApiResponse<Response_Account> delete(
            @RequestParam(value = "id", required = false) Integer id) {
        service_Account.updateStatus(id, false);
        return ApiResponse.<Response_Account>build()
                .message("Xóa nhân viên thành công");
    }

}