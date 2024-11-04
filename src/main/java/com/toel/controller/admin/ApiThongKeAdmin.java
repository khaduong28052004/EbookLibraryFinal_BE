package com.toel.controller.admin;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.Response_Account;
import com.toel.dto.admin.response.ThongKe.Response_TKDT_Seller;
import com.toel.service.admin.Service_Thongke;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/orderstatistacal")
public class ApiThongKeAdmin {
    @Autowired
    Service_Thongke service_Thongke;

    @GetMapping
    public ApiResponse<PageImpl<Response_TKDT_Seller>> getAllTKDT_Seller(
            @RequestParam(value = "dateStart", required = false) LocalDate dateStart,
            @RequestParam(value = "dateEnd", required = false) LocalDate dateEnd,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "gender", required = false) Boolean gender,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_TKDT_Seller>>build()
                .result(service_Thongke.get_TKDT_Seller(dateStart, dateEnd, search, gender, page, size, sortBy,
                        sortColumn));
    }

    
}