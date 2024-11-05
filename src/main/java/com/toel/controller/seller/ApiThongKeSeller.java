package com.toel.controller.seller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.response.Response_ThongKeBill;
import com.toel.dto.seller.response.Response_ThongKeKhachHang;
import com.toel.dto.seller.response.Response_ThongKeSanPham;
import com.toel.service.seller.Service_ThongKeSeller;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/thongKe")
public class ApiThongKeSeller {
        @Autowired
        Service_ThongKeSeller service_ThongKe;

        @GetMapping("/bill")
        public ApiResponse<Response_ThongKeBill> getThongKeBill(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
                        @RequestParam(value = "dateStart", required = false) Date dateStart,
                        @RequestParam(value = "dateEnd", required = false) Date dateEnd,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
                return ApiResponse.<Response_ThongKeBill>build()
                                .result(Response_ThongKeBill.builder()
                                                .chietKhau(service_ThongKe.getChietKhau())
                                                .tongDoanhSo(service_ThongKe.getTongDoanhSo(account_id, dateStart,
                                                                dateEnd))
                                                .tongDoanhThu(service_ThongKe.getTongDoanhThu(account_id, dateStart,
                                                                dateEnd))
                                                .bill(service_ThongKe.getListThongKeBill(account_id, dateStart, dateEnd,
                                                                page, size, sortBy,
                                                                sortColumn))
                                                .build());
        }

        @GetMapping("/khachHang")
        public ApiResponse<PageImpl<Response_ThongKeKhachHang>> getThongKeKhachHang(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "name") String sortColumn) {
                return ApiResponse.<PageImpl<Response_ThongKeKhachHang>>build()
                                .result(service_ThongKe.getListThongKeKhachHang(account_id, page, size, sortBy,
                                                sortColumn));
        }

        @GetMapping("/sanPham")
        public ApiResponse<PageImpl<Response_ThongKeSanPham>> getThongKeSanPham(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "name") String sortColumn) {
                return ApiResponse.<PageImpl<Response_ThongKeSanPham>>build()
                                .result(service_ThongKe.getListThongKeSanPham(account_id, page, size, sortBy,
                                sortColumn));
        }
}
