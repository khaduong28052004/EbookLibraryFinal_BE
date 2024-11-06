package com.toel.controller.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.ThongKe.Response_TKDT_Seller;
import com.toel.dto.admin.response.ThongKe.Response_TK_Product;
import com.toel.service.admin.Thongke.Service_ThongKe_Product;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/orderstatistacal/product")
public class ApiThongKeProduct {
    @Autowired
    Service_ThongKe_Product service_ThongKe_Product;

    @GetMapping
    public ApiResponse<PageImpl<Response_TK_Product>> getAllTKDT_Seller(
            @RequestParam(value = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
            @RequestParam(value = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd,
            @RequestParam(value = "option", defaultValue = "sanpham") String option,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_TK_Product>>build()
                .result(service_ThongKe_Product.get_TKDT_Product(dateStart, dateEnd, option, search, page, size, sortBy,
                        sortColumn));
    }
}
