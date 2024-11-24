package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.Account.Request_AccountCreate;
import com.toel.dto.admin.response.Response_Account;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.repository.AccountRepository;
import com.toel.service.admin.Service_Account;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
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
    @Autowired
    AccountRepository accountRepository;

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
            pageImpl = service_Account.getAllSeller("SELLER", search, gender, page, size, sortBy, sortColumn);
        } else {
            pageImpl = null;
        }
        return ApiResponse.<PageImpl<?>>build()
                .result(pageImpl);
    }

    @GetMapping("seller/notbrowse")
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
        String message = checkName(entity.getUsername(), entity.getPhone(), entity.getEmail());
        if (message != null) {
            throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, message);
        }
        return ApiResponse.<Response_Account>build()
                .result(service_Account.create("ADMINV1", entity))
                .message("Thêm nhân viên thành công");
    }

    @PutMapping("adminv1")
    public ApiResponse<Response_Account> put(@RequestBody @Valid Request_AccountCreate entity) {
        String message = checkName(entity.getUsername(), entity.getPhone(), entity.getEmail());
        if (message != null) {
            throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, message);
        }
        return ApiResponse.<Response_Account>build()
                .result(service_Account.create("ADMINV1", entity))
                .message("Thêm nhân viên thành công");
    }

    @PutMapping
    public ApiResponse<Response_Account> putStatus(@RequestParam(value = "id", required = false) Integer id) {
        Response_Account entity = service_Account.updateStatus(id, null);
        return ApiResponse.<Response_Account>build()
                .result(entity)
                .message(entity.isStatus() ? "Khôi phục hoạt động thành công"
                        : "Ngừng hoạt động thành công");
    }

    @PutMapping("seller/browse")
    public ApiResponse<Response_Account> putActive(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "status", required = false) Boolean status) {
        Response_Account entity = service_Account.updateActive(id, status);
        return ApiResponse.<Response_Account>build()
                .message(entity.isStatus() ? "Duyệt thành công" : "Hủy thành công")
                .result(entity);
    }

    private String checkName(String name, String sdt, String email) {
        boolean checkName = accountRepository.existsByUsernameIgnoreCase(name);
        boolean checkPhone = accountRepository.existsByPhoneIgnoreCase(sdt);
        boolean checkEmail = accountRepository.existsByEmailIgnoreCase(email);
        if (checkName) {
            return "Tài khoản";
        } else if (checkPhone) {
            return "Số điện thoại";
        } else if (checkEmail) {
            return "Email";
        } else {
            return null;
        }
    }
    // @DeleteMapping
    // public ApiResponse<Response_Account> delete(
    // @RequestParam(value = "id", required = false) Integer id) {
    // service_Account.updateStatus(id, false);
    // return ApiResponse.<Response_Account>build()
    // .message("Xóa nhân viên thành công");
    // }

}