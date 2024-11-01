package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.Account.Request_AccountCreate;
import com.toel.dto.admin.request.Permission.Request_PermissionCreate;
import com.toel.dto.admin.response.Response_Account;
import com.toel.dto.admin.response.Response_Permission;
import com.toel.dto.admin.response.Response_Permission_Role;
import com.toel.service.admin.Service_Premisson;
import com.toel.service.admin.Service_RolePermission;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/permission")
public class ApiPermissionAdmin {
    @Autowired
    Service_Premisson service_Premisson;

    @GetMapping
    public ApiResponse<PageImpl<Response_Permission>> getAllNhanVApiResponse(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_Permission>>build()
                .result(service_Premisson.getAll(search, role, page, size, sortBy, sortColumn));
    }

    @GetMapping("notrole")
    public ApiResponse<PageImpl<Response_Permission>> getAllNotRole(
            @RequestParam(value = "role", required = false) Integer role,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_Permission>>build()
                .result(service_Premisson.getAllNotRole(role, page, size, sortBy, sortColumn));
    }

    @GetMapping("id")
    public ApiResponse<Response_Permission_Role> getById(@RequestParam(value = "id", required = false) Integer id) {
        return ApiResponse.<Response_Permission_Role>build()
                .result(service_Premisson.getByID(id));
    }
//
    @PostMapping
    public ApiResponse<Response_Permission> post(@RequestBody @Valid Request_PermissionCreate entity) {
        return ApiResponse.<Response_Permission>build()
                .result(service_Premisson.create(entity));
    }
//
    @DeleteMapping
    public ApiResponse<Response_Permission> delete(
            @RequestParam(value = "id", required = false) Integer id) {
        service_Premisson.delete(id);
        return ApiResponse.<Response_Permission>build()
                .message("Xóa nhân viên thành công");
    }

}
