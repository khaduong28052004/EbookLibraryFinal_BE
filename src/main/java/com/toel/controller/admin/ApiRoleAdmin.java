package com.toel.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.Role.RequestRoleCreate;
import com.toel.dto.admin.request.Role.RequestRoleUpdate;
import com.toel.dto.admin.response.Response_Role;
import com.toel.service.admin.Service_Role;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/role")
public class ApiRoleAdmin {
    @Autowired
    Service_Role service_Role;

    @GetMapping
    public ApiResponse<PageImpl<Response_Role>> getRoleNhanVien(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_Role>>build()
                .result(service_Role.getRoleNhanVien(search, page, size, sortBy, sortColumn));
    }

    @GetMapping("/listnotnhanvien")
    public ApiResponse<List<Response_Role>> getlistRoleNotNhanVien() {
        return ApiResponse.<List<Response_Role>>build()
                .result(service_Role.getlistRoleNotNhanVien());
    }

    @PostMapping
    public ApiResponse<Response_Role> create(@RequestBody @Valid RequestRoleCreate entity) {
        return ApiResponse.<Response_Role>build()
                .message("Thêm quyền thành công")
                .result(service_Role.create(entity));
    }

    @PutMapping
    public ApiResponse<Response_Role> update(@RequestBody @Valid RequestRoleUpdate entity) {
        return ApiResponse.<Response_Role>build()
                .message("Cập nhật quyền thành công")
                .result(service_Role.update(entity));
    }

    @DeleteMapping
    public ApiResponse<Response_Role> delete(
            @RequestParam(value = "id", required = false) Integer id) {
        service_Role.delete(id);
        return ApiResponse.<Response_Role>build()
                .message("Xóa quyền thành công");
    }
}
