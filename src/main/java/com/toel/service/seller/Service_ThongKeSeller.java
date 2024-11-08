package com.toel.service.seller;

import java.util.ArrayList;
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
import com.toel.dto.seller.response.Response_ThongKeKhachHang;
import com.toel.dto.seller.response.Response_ThongKeSanPham;
import com.toel.mapper.BillMapper;
import com.toel.model.Bill;
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
        Integer chietKhau = discountRateRepository.getChietKhau();
        return chietKhau == null ? 0 : chietKhau;
    }

    public Double getTongDoanhSo(
            Integer account_id, Date dateStart, Date dateEnd) {
        Double tongDoanhSo = billRepository.getTongDoanhSo(account_id, dateStart == null ? new Date() : dateStart,
                dateEnd == null ? new Date() : dateEnd);
        return tongDoanhSo == null ? 0.0 : tongDoanhSo;
    }

    public Double getTongDoanhThu(
            Integer account_id, Date dateStart, Date dateEnd) {
        Double tongDoanhThu = billRepository.getTongDoanhThu(account_id, dateStart == null ? new Date() : dateStart,
                dateEnd == null ? new Date() : dateEnd);
        return tongDoanhThu == null ? 0.0 : tongDoanhThu;
    }

    public PageImpl<Response_Bill> getListThongKeBill(
            Integer account_id, Date dateStart, Date dateEnd, Integer page, Integer size, boolean sortBy,
            String sortColumn) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Bill> pageBill = billRepository.getListThongKeBill(account_id,
                dateStart == null ? new Date() : dateStart,
                dateEnd == null ? new Date() : dateEnd, pageable);
        List<Response_Bill> list = pageBill.stream()
                .map(bill -> billMapper.response_Bill(bill))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageBill.getTotalElements());
    }

    public PageImpl<Response_ThongKeKhachHang> getListThongKeKhachHang(
            Integer account_id, Integer page, Integer size, boolean sortBy, String sortColumn) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        List<Object[]> results = billRepository.getListThongKeKhachHang(account_id);
        List<Response_ThongKeKhachHang> pageKhachHang = new ArrayList<>();
        for (Object[] result : results) {
            String name = (String) result[0]; // fullname
            Integer soSanPham = ((Number) result[1]).intValue(); // SUM(bd.quantity)
            Integer luotMua = ((Number) result[2]).intValue(); // COUNT(b)
            Integer luotDanhGia = ((Number) result[3]).intValue(); // COUNT(e)
            double soTien = ((Number) result[4]).doubleValue(); // SUM(b.totalPrice)

            pageKhachHang.add(new Response_ThongKeKhachHang(name, soSanPham, luotMua, luotDanhGia, soTien));
        }

        return new PageImpl<>(pageKhachHang, pageable, results.size());
    }

    public PageImpl<Response_ThongKeSanPham> getListThongKeSanPham(
            Integer account_id, Integer page, Integer size, boolean sortBy, String sortColumn) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        List<Object[]> results = billRepository.getListThongKeSanPham(account_id);
        List<Response_ThongKeSanPham> pageSanPham = new ArrayList<>();
        for (Object[] result : results) {
            String name = (String) result[0];
            String theLoai = (String) result[1];
            Integer luotBan = ((Number) result[2]).intValue();
            Integer luotDanhGia = ((Number) result[3]).intValue();;
            double trungBinhDanhGia = ((Number) result[4]).doubleValue();
            Integer luotYeuThich = ((Number) result[5]).intValue();
            pageSanPham.add(
                    new Response_ThongKeSanPham(name, theLoai, luotBan, luotDanhGia, trungBinhDanhGia, luotYeuThich));
        }
        return new PageImpl<>(pageSanPham, pageable, results.size());
    }

}
