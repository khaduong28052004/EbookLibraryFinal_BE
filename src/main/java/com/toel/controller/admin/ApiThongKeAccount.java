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
import com.toel.dto.admin.response.ThongKe.Page_TKDT_Seller;
import com.toel.dto.admin.response.ThongKe.Response_TKDT_Seller;
import com.toel.dto.admin.response.ThongKe.Response_TK_Account;
import com.toel.dto.admin.response.ThongKe.Response_TK_Seller;
import com.toel.service.admin.Thongke.Service_ThongKe_KhachHang;
import com.toel.service.admin.Thongke.Service_ThongKe_Seller;
import com.toel.service.admin.Thongke.Service_Thongke_DoanhThu;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/orderstatistacal")
public class ApiThongKeAccount {
        @Autowired
        Service_ThongKe_KhachHang service_ThongKe_KhachHang;
        @Autowired
        Service_ThongKe_Seller service_ThongKe_Seller;
        @Autowired
        Service_Thongke_DoanhThu service_Thongke_DoanhThu;
        @GetMapping("revenue")
        public ApiResponse<Page_TKDT_Seller> getAllTKDT_Seller(
                        @RequestParam(value = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
                        @RequestParam(value = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "gender", required = false) Boolean gender,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
                double[] currentMonthValues = service_Thongke_DoanhThu.calculateMonthlyRevenue(dateStart, dateEnd);
                return ApiResponse.<Page_TKDT_Seller>build()
                                .result(Page_TKDT_Seller.builder()
                                                .tkdt_seller((service_Thongke_DoanhThu.get_TKDT_Seller(dateStart,
                                                                dateEnd, search, gender,
                                                                page, size,
                                                                sortBy,
                                                                sortColumn)))
                                                .tongShop((int) currentMonthValues[0])
                                                .tongDoanhThu(currentMonthValues[1])
                                                .tongPhi(currentMonthValues[2])
                                                .tongLoiNhuan(currentMonthValues[3])
                                                .build());
        }

        @GetMapping("account")
        public ApiResponse<PageImpl<Response_TK_Account>> getAll(
                        @RequestParam(value = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
                        @RequestParam(value = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "gender", required = false) Boolean gender,
                        @RequestParam(value = "option", required = false) String option,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
                return ApiResponse.<PageImpl<Response_TK_Account>>build()
                                .result(service_ThongKe_KhachHang.get_TK_KhachHang(dateStart, dateEnd, option, search,
                                                gender, page, size, sortBy, sortColumn));
        }

        @GetMapping("seller")
        public ApiResponse<PageImpl<Response_TK_Seller>> get_TK_Seller(
                        @RequestParam(value = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
                        @RequestParam(value = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "option", required = false) String option,
                        @RequestParam(value = "gender", required = false) Boolean gender,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
                return ApiResponse.<PageImpl<Response_TK_Seller>>build()
                                .result(service_ThongKe_Seller.get_TK_Seller(dateStart, dateEnd, option, search, gender,
                                                page, size, sortBy, sortColumn));
        }
}
