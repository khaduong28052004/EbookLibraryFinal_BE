package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.Account.Request_AccountCreate;
import com.toel.dto.admin.response.Response_Account;
import com.toel.service.admin.Service_NhanVien;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/nhanvien")
@Validated
public class ApiNhanVien {
    @Autowired
    Service_NhanVien service_NhanVien;

    @GetMapping
    public ApiResponse<PageImpl<Response_Account>> getAllNhanVApiResponse(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "gender", required = false) Boolean gender,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_Account>>build()
                .result(service_NhanVien.getAll(search, gender, page, size, sortBy, sortColumn));
    }

    @PostMapping
    public ApiResponse<Response_Account> post(@RequestBody @Valid Request_AccountCreate entity) {
        return ApiResponse.<Response_Account>build()
                .result(service_NhanVien.create(entity));
    }

    @DeleteMapping
    public ApiResponse<Response_Account> delete(
            @RequestParam(value = "id", required = false) Integer id) {
        service_NhanVien.delete(id);
        return ApiResponse.<Response_Account>build()
                .message("Xóa nhân viên thành công");
    }

}