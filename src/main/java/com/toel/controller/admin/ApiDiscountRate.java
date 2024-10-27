package com.toel.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.Response_DiscountRate;
import com.toel.service.admin.DiscountRateService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/chietkhau")
public class ApiDiscountRate {
    @Autowired
    DiscountRateService discountReateService;

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
                System.out.println("searchDateTime: "+searchDateTime);
            } catch (Exception e) {
                return ApiResponse.<PageImpl<Response_DiscountRate>>build()
                .code(500)
                .message("Ngày không đúng định dạng");
            }
        }
        return ApiResponse.<PageImpl<Response_DiscountRate>>build()
                .result(discountReateService.getAll(page, size, searchDateTime,sortBy,sortColumn));
    }
}
