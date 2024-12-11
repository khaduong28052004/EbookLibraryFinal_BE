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
import com.toel.dto.admin.response.Response_ProductReport;
import com.toel.service.admin.Service_AccountReport;
import com.toel.service.admin.Service_ProductReport;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/report")
public class ApiReport {
    @Autowired
    Service_AccountReport service_AccountReport;
    @Autowired
    Service_ProductReport service_ProductReport;

    @GetMapping("account")
    public ApiResponse<PageImpl<Response_AccountReport>> getAllAccount(
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

    @GetMapping("account/id")
    public ApiResponse<Response_AccountReport> getByIdAccount(
            @RequestParam(value = "id", required = false) Integer id) {
        return ApiResponse.<Response_AccountReport>build()
                .result(service_AccountReport.getId(id));
    }

    @PutMapping("account")
    public ApiResponse<Response_AccountReport> putStatusAccount(
            @RequestParam(value = "accountID", required = false) Integer account,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "contents", required = false) String contents) {
        Response_AccountReport entity = service_AccountReport.updateStatus(id, contents, account);
        return ApiResponse.<Response_AccountReport>build()
                .result(entity)
                .message("Giải quyết report thành công");

    }

    @GetMapping("product")
    public ApiResponse<PageImpl<Response_ProductReport>> getAllProduct(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "option", defaultValue = "macdinh") String option,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        try {
            return ApiResponse.<PageImpl<Response_ProductReport>>build()
                    .result(service_ProductReport.getAll(option, page, size, sortBy, sortColumn, search));
        } catch (Exception e) {
            return ApiResponse.<PageImpl<Response_ProductReport>>build()
                    .message(e.getMessage());
        }
    }

    @GetMapping("product/id")
    public ApiResponse<Response_ProductReport> getByIdProduct(
            @RequestParam(value = "id", required = false) Integer id) {
        return ApiResponse.<Response_ProductReport>build()
                .result(service_ProductReport.getId(id));
    }

    @PutMapping("product")
    public ApiResponse<Response_ProductReport> putStatusProduct(
            @RequestParam(value = "accountID", required = false) Integer account,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "contents", required = false) String contents) {
        Response_ProductReport entity = service_ProductReport.updateStatus(id, contents, account);
        return ApiResponse.<Response_ProductReport>build()
                .result(entity)
                .message("Giải quyết report thành công");

    }
}
