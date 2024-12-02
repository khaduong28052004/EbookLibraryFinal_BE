package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.Response_AccountReport;
import com.toel.service.admin.Service_AccountReport;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/report/account")
public class ApiAccountReport {
    @Autowired
    Service_AccountReport service_AccountReport;

    @GetMapping
    public ApiResponse<PageImpl<Response_AccountReport>> getAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "option", defaultValue = "macdinh") String option,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        try {
            return ApiResponse.<PageImpl<Response_AccountReport>>build()
                    .result(service_AccountReport.getAll(option, page, size, sortBy, sortColumn, search));
        } catch (Exception e) {
            return ApiResponse.<PageImpl<Response_AccountReport>>build()
                    .message(e.getMessage());
        }
    }

    @GetMapping("id")
    public ApiResponse<Response_AccountReport> getById(@RequestParam(value = "id", required = false) Integer id) {
        return ApiResponse.<Response_AccountReport>build()
                .result(service_AccountReport.getId(id));
    }

    @PutMapping
    public ApiResponse<Response_AccountReport> putStatus(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "contents", required = false) String contents) {
        Response_AccountReport entity = service_AccountReport.updateStatus(id, contents);
        return ApiResponse.<Response_AccountReport>build()
                .result(entity)
                .message("Giải quyết report thành công");

    }
}
