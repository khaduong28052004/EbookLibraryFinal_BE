package com.toel.service.seller;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.seller.response.Response_DoanhSo;
import com.toel.dto.seller.response.Response_DoanhThu;
import com.toel.dto.seller.response.Response_Year;
import com.toel.repository.BillRepository;
import com.toel.repository.LikeRepository;

@Service
public class Service_HomeSeller {
    @Autowired
    BillRepository billRepository;
    @Autowired
    LikeRepository likeRepository;

    public Integer getLike(Integer account_id) {
        Integer likeCount = likeRepository.getLike(account_id);
        return likeCount != null ? likeCount : 0;
    }

    public Integer getDonChoDuyet(Integer account_id) {
        Integer donChoDuyetCount = billRepository.getDonChoDuyet(account_id);
        return donChoDuyetCount != null ? donChoDuyetCount : 0;
    }

    public Double getDoanhSo(Integer account_id) {
        Double doanhSo = billRepository.getDoanhSo(account_id);
        return doanhSo != null ? doanhSo : 0.0;
    }

    public Double getDoanhThu(Integer account_id) {
        Double doanhThu = billRepository.getDoanhThu(account_id);
        return doanhThu != null ? doanhThu : 0.0;
    }

    public List<Response_DoanhSo> getListDoanhSo(Integer year, Integer account_id) {
        List<Object[]> listDoanhSo = billRepository.getListDoanhSo(year, account_id);

        List<Response_DoanhSo> defaultList = IntStream.range(0, 12)
                .mapToObj(i -> new Response_DoanhSo(0.0, i + 1))

                .collect(Collectors.toList());

        // Nếu có dữ liệu, cập nhật doanh số cho từng tháng
        for (Object[] record : listDoanhSo) {
            int month = (Integer) record[0];
            double totalRevenue = (Double) record[1];
            defaultList.set(month - 1, new Response_DoanhSo(totalRevenue, month));
        }

        return defaultList; // Trả về danh sách đã được cập nhật
    }

    public List<Response_DoanhThu> getListDoanhThu(Integer year, Integer account_id) {
        List<Object[]> listDoanhThu = billRepository.getListDoanhThu(year, account_id);

        List<Response_DoanhThu> defaultList = IntStream.range(0, 12)
                .mapToObj(i -> new Response_DoanhThu(0.0, i + 1))
                .collect(Collectors.toList());

        for (Object[] record : listDoanhThu) {
            int month = (Integer) record[0];
            double revenue = (Double) record[1];
            defaultList.set(month - 1, new Response_DoanhThu(revenue, month));
        }
        return defaultList;
    }

    public List<Response_Year> getYears(Integer account_id) {
        // Lấy danh sách các năm khác nhau từ repository
        List<Integer> years = billRepository.getDistinctYears(account_id);

        // Nếu danh sách trống, trả về năm hiện tại, ngược lại trả về danh sách các năm
        return years.isEmpty()
                ? Collections.singletonList(new Response_Year(Year.now().getValue()))
                : years.stream().map(Response_Year::new).collect(Collectors.toList());
    }
}
