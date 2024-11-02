package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.Response_Permission;
import com.toel.dto.admin.response.Response_Role;
import com.toel.service.admin.Service_Role;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/role")
public class ApiRoleAdmin {
    @Autowired
    Service_Role service_Role;

    @GetMapping
    public ApiResponse<PageImpl<Response_Role>> getAllRoleNotPermissonRole(
            @RequestParam(value = "idPermission", required = false) Integer idPermission,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_Role>>build()
                .result(service_Role.getRoleNotPermissonRole(idPermission, page, size, sortBy, sortColumn));
    }
}
