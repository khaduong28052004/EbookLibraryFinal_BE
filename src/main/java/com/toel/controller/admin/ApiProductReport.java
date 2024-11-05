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
import com.toel.dto.admin.response.Response_Permission_Role;
import com.toel.dto.admin.response.Response_ProductReport;
import com.toel.service.admin.Service_ProductReport;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/productreport")
public class ApiProductReport {
    @Autowired
    Service_ProductReport service_ProductReport;

    @GetMapping
    public ApiResponse<PageImpl<Response_ProductReport>> getAllNhanVApiResponse(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_ProductReport>>build()
                .result(service_ProductReport.getAll(page, size, sortBy, sortColumn, search));
    }

    @GetMapping("id")
    public ApiResponse<Response_ProductReport> getById(@RequestParam(value = "id", required = false) Integer id) {
        return ApiResponse.<Response_ProductReport>build()
                .result(service_ProductReport.getId(id));
    }
}
