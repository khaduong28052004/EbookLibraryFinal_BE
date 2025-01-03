package com.toel.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.DiscountRate.Request_DiscountRateCreate;
import com.toel.dto.admin.request.DiscountRate.Request_DiscountRateUpdate;
import com.toel.dto.admin.response.Response_DiscountRate;
import com.toel.service.admin.Service_DiscountRate;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/chietkhau")
public class ApiDiscountRateAdmin {
    @Autowired
    Service_DiscountRate discountReateService;

    @GetMapping
    public ApiResponse<PageImpl<Response_DiscountRate>> getAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {

        LocalDateTime searchDateTime = null;
        if (search != null && !search.isEmpty()) {
            try {
                // Chọn định dạng ngày mà bạn muốn (ví dụ: "yyyy-MM-dd")
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate date = LocalDate.parse(search, formatter);
                searchDateTime = date.atStartOfDay(); // Chuyển đổi LocalDate thành LocalDateTime
                System.out.println("searchDateTime: " + searchDateTime);
            } catch (Exception e) {
                return ApiResponse.<PageImpl<Response_DiscountRate>>build()
                        .code(500)
                        .message("Ngày không đúng định dạng");
            }
        }
        return ApiResponse.<PageImpl<Response_DiscountRate>>build()
                .result(discountReateService.getAll(page, size, searchDateTime, sortBy, sortColumn));
    }

    @GetMapping("id")
    public ApiResponse<Response_DiscountRate> getId(@RequestParam(value = "id", required = false) Integer id) {
        return ApiResponse.<Response_DiscountRate>build()
                .result(discountReateService.getById(id));
    }

    @PostMapping
    public ApiResponse<Response_DiscountRate> post(
            @RequestParam(value = "accountID", required = false) Integer account,
            @RequestBody @Valid Request_DiscountRateCreate entity) {
        return ApiResponse.<Response_DiscountRate>build()
                .message("Thêm chiết khẩu thành công")
                .result(discountReateService.create(entity, account));
    }

    @PutMapping
    public ApiResponse<Response_DiscountRate> put(
            @RequestParam(value = "accountID", required = false) Integer account,
            @RequestBody @Valid Request_DiscountRateUpdate entity) {
        return ApiResponse.<Response_DiscountRate>build()
                .message("Cập nhật chiết khẩu thành công")
                .result(discountReateService.update(entity, account));
    }

    @DeleteMapping
    public ApiResponse<Response_DiscountRate> delete(
            @RequestParam(value = "accountID", required = false) Integer account,
            @RequestParam(value = "id", required = false) Integer id) {
        discountReateService.delete(id, account);
        return ApiResponse.<Response_DiscountRate>build()
                .message("Xóa thành công");
    }

}
