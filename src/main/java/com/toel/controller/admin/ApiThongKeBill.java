package com.toel.controller.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.ThongKe.Page_TK_Bill;
import com.toel.service.admin.Thongke.Service_ThongKe_DonHang;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/orderstatistacal/bill")
public class ApiThongKeBill {
    @Autowired
    Service_ThongKe_DonHang service_Thongke;

    @GetMapping
    public ApiResponse<Page_TK_Bill> getAllTKDT_Seller(
            @RequestParam(value = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
            @RequestParam(value = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd,
            @RequestParam(value = "orderStatusId", required = false) Integer orderStatusId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<Page_TK_Bill>build()
                .result(service_Thongke.get_TKDT_DonHang(dateStart, dateEnd, orderStatusId, page, size, sortBy,
                        sortColumn));
    }
}
