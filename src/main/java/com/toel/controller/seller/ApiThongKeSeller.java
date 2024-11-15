package com.toel.controller.seller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.toel.dto.seller.response.Response_ListKhachHang;
import com.toel.dto.seller.response.Response_ListSanPham;
import com.toel.dto.seller.response.Response_ThonKeSanPham;
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
                        @RequestParam(value = "dateStart", required = false) String dateStart,
                        @RequestParam(value = "dateEnd", required = false) String dateEnd,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = null;
                Date endDate = null;
                try {
                        startDate = (dateStart != null && !dateStart.isEmpty()) ? formatter.parse(dateStart)
                                        : null;
                        endDate = (dateEnd != null && !dateEnd.isEmpty()) ? formatter.parse(dateEnd) : null;
                } catch (ParseException e) {
                        e.printStackTrace();
                }

                return ApiResponse.<Response_ThongKeBill>build()
                                .result(Response_ThongKeBill.builder()
                                                .chietKhau(service_ThongKe.getChietKhau())
                                                .tongDoanhSo(service_ThongKe.getTongDoanhSo(account_id, startDate,
                                                                endDate))
                                                .tongDoanhThu(service_ThongKe.getTongDoanhThu(account_id, startDate,
                                                                endDate))
                                                .bill(service_ThongKe.getListThongKeBill(account_id, startDate, endDate,
                                                                page, size, sortBy, sortColumn))
                                                .build());
        }

        @GetMapping("/khachHang")
        public ApiResponse<Response_ThongKeKhachHang> getThongKeKhachHang(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "b.account.fullname") String sortColumn,
                        @RequestParam(value = "search", required = false) String search) {
                return ApiResponse.<Response_ThongKeKhachHang>build()
                                .result(Response_ThongKeKhachHang.builder()
                                                .tongSoLuotMua(service_ThongKe.getTongSoLuotMua(account_id, search))
                                                .tongSoSP(service_ThongKe.getTongSoSP(account_id, search))
                                                .tongSoLuotDanhGia(service_ThongKe.getTongSoLuotDanhGia(account_id,
                                                                search))
                                                .tongSoTien(service_ThongKe.getTongTien(account_id, search))
                                                .khachHang(service_ThongKe.getListThongKeKhachHang(account_id, page,
                                                                size, sortBy, sortColumn, search))
                                                .build());
        }

        @GetMapping("/sanPham")
        public ApiResponse<Response_ThonKeSanPham> getThongKeSanPham(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "p.name") String sortColumn,
                        @RequestParam(value = "search", required = false) String search) {
                return ApiResponse.<Response_ThonKeSanPham>build()
                                .result(Response_ThonKeSanPham.builder()
                                                .tongLuotBan(service_ThongKe.getTongLuotBanSanPham(account_id, search))
                                                .tongLuotDanhGia(service_ThongKe.getTongLuotDanhGiaSanPham(account_id,
                                                                search))
                                                .tongTrungBinhDanhGia(service_ThongKe
                                                                .getTongTrungBinhDanhGiaSanPham(account_id, search))
                                                .tongLuotYeuThich(
                                                                service_ThongKe.getTongLuotYeuThichSanPham(account_id,
                                                                                search))
                                                .sanPham(service_ThongKe.getListThongKeSanPham(account_id, page, size,
                                                                sortBy, sortColumn, search))
                                                .build());
        }
}
