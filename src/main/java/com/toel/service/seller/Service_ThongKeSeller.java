package com.toel.service.seller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.seller.response.Response_Bill;
import com.toel.dto.seller.response.Response_ListKhachHang;
import com.toel.dto.seller.response.Response_ListSanPham;
import com.toel.mapper.BillMapper;
import com.toel.model.Bill;
import com.toel.model.DiscountRate;
import com.toel.repository.BillRepository;
import com.toel.repository.DiscountRateRepository;

@Service
public class Service_ThongKeSeller {
    @Autowired
    BillRepository billRepository;
    @Autowired
    DiscountRateRepository discountRateRepository;
    @Autowired
    BillMapper billMapper;

    public Integer getChietKhau() {
        return discountRateRepository.getChietKhau()
                .map(DiscountRate::getDiscount)
                .orElse(0);
    }

    public Double getTongDoanhSo(
            Integer account_id, Date dateStart, Date dateEnd) {
        Double tongDoanhSo = billRepository.getTongDoanhSo(account_id, dateStart, dateEnd);
        return tongDoanhSo == null ? 0.0 : tongDoanhSo;
    }

    public Double getTongDoanhThu(
            Integer account_id, Date dateStart, Date dateEnd) {
        Double tongDoanhThu = billRepository.getTongDoanhThu(account_id, dateStart, dateEnd);
        return tongDoanhThu == null ? 0.0 : tongDoanhThu;
    }

    public Integer getTongSoLuotMua(
            Integer account_id, String search) {
        Integer tongSoLuotMua = billRepository.tongSoLuotMua(account_id, search);
        return tongSoLuotMua == null ? 0 : tongSoLuotMua;
    }

    public Integer getTongSoSP(
            Integer account_id, String search) {
        Integer tongSoSP = billRepository.tongSoSP(account_id, search);
        return tongSoSP == null ? 0 : tongSoSP;
    }

    public Integer getTongSoLuotDanhGia(
            Integer account_id, String search) {
        Integer tongSoLuotDanhGia = billRepository.tongSoLuotDanhGia(account_id, search);
        return tongSoLuotDanhGia == null ? 0 : tongSoLuotDanhGia;
    }

    public Double getTongTien(
            Integer account_id, String search) {
        Double tongTien = billRepository.tongSotTien(account_id, search);
        return tongTien == null ? 0.0 : tongTien;
    }

    public Integer getTongLuotBanSanPham(
            Integer account_id, String search) {
        Integer tongLuotBanSanPham = billRepository.tongLuotBanSanPham(account_id, search);
        return tongLuotBanSanPham == null ? 0 : tongLuotBanSanPham;
    }

    public Integer getTongLuotDanhGiaSanPham(
            Integer account_id, String search) {
        Integer tongLuotDanhGia = billRepository.tongLuotDanhGia(account_id, search);
        return tongLuotDanhGia == null ? 0 : tongLuotDanhGia;
    }

    public Integer getTongLuotYeuThichSanPham(
            Integer account_id, String search) {
        Integer tongLuotYeuThichSanPham = billRepository.tongLuotYeuThich(account_id, search);
        return tongLuotYeuThichSanPham == null ? 0 : tongLuotYeuThichSanPham;
    }

    public Double getTongTrungBinhDanhGiaSanPham(
            Integer account_id, String search) {
        Double tongTrungBinhDanhGia = billRepository.tongTrungBinhLuotDanhGia(account_id, search);
        return tongTrungBinhDanhGia == null ? 0 : tongTrungBinhDanhGia;
    }

    public PageImpl<Response_Bill> getListThongKeBill(
            Integer account_id, Date dateStart, Date dateEnd, Integer page, Integer size, boolean sortBy,
            String sortColumn) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Bill> pageBill = billRepository.getListThongKeBill(account_id, dateStart, dateEnd, pageable);
        List<Response_Bill> list = pageBill.stream()
                .map(bill -> billMapper.response_Bill(bill))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageBill.getTotalElements());
    }

    public PageImpl<Response_ListKhachHang> getListThongKeKhachHang(
            Integer account_id, Integer page, Integer size, boolean sortBy, String sortColumn, String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Object[]> results = billRepository.getListThongKeKhachHang(account_id, search, pageable);

        List<Response_ListKhachHang> pageKhachHang = results.stream().map(result -> {
            String name = (String) result[0];
            Integer soSanPham = ((Number) result[1]).intValue();
            Integer luotMua = ((Number) result[2]).intValue();
            Integer luotDanhGia = ((Number) result[3]).intValue();
            double soTien = ((Number) result[4]).doubleValue();

            return new Response_ListKhachHang(name, soSanPham, luotMua, luotDanhGia, soTien);
        }).collect(Collectors.toList());

        return new PageImpl<>(pageKhachHang, pageable, results.getTotalElements());
    }

    public PageImpl<Response_ListSanPham> getListThongKeSanPham(
            Integer account_id, Integer page, Integer size, boolean sortBy, String sortColumn, String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Object[]> results = billRepository.getListThongKeSanPham(account_id, search, pageable);

        List<Response_ListSanPham> pageSanPham = results.stream().map(result -> {
            String name = result[0] != null ? (String) result[0] : "";
            String theLoai = result[1] != null ? (String) result[1] : "";
            Integer luotBan = result[2] != null ? ((Number) result[2]).intValue() : 0;
            Integer luotDanhGia = result[3] != null ? ((Number) result[3]).intValue() : 0;
            double trungBinhDanhGia = (result[4] != null && result[4] instanceof Number)
                    ? ((Number) result[4]).doubleValue()
                    : 0.0;
            Integer luotYeuThich = result[5] != null ? ((Number) result[5]).intValue() : 0;

            return new Response_ListSanPham(name, theLoai, luotBan, luotDanhGia, trungBinhDanhGia, luotYeuThich);
        }).collect(Collectors.toList());

        return new PageImpl<>(pageSanPham, pageable, results.getTotalElements());
    }

}
