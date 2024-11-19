package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.RolePermission.Request_RolePermissionCreate;
import com.toel.dto.admin.response.Response_RolePermission;
import com.toel.service.admin.Service_RolePermission;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/rolepermission")
public class ApiRolePermissionAdmin {
    @Autowired
    Service_RolePermission service_RolePermission;

    @PostMapping
    public ApiResponse<Response_RolePermission> create(@RequestBody @Valid Request_RolePermissionCreate entity) {
        return ApiResponse.<Response_RolePermission>build()
                .message("Thêm quyền thành công")
                .result(service_RolePermission.create(entity));
    }

    @DeleteMapping
    public ApiResponse<Response_RolePermission> delete(@RequestParam(value = "id", required = false) Integer id) {
        service_RolePermission.delete(id);
        return ApiResponse.<Response_RolePermission>build()
                .message("Xóa quyền thành công");
    }

}
