package com.toel.service.seller;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public double getDoanhSo(Integer account_id) {
        Double doanhSo = billRepository.getDoanhSo(account_id);
        return doanhSo != null ? doanhSo : 0.0;
    }

    public double getDoanhThu(Integer account_id) {
        Double doanhThu = billRepository.getDoanhThu(account_id);
        return doanhThu != null ? doanhThu : 0.0;
    }

    public List<Response_DoanhSo> getListDoanhSo(Integer year, Integer account_id) {
        List<Response_DoanhSo> listDoanhSo = billRepository.getListDoanhSo(year, account_id);
        return listDoanhSo != null ? listDoanhSo : new ArrayList<>();
    }

    public List<Response_DoanhThu> getListDoanhThu(Integer year, Integer account_id) {
        List<Response_DoanhThu> listDoanhThu = billRepository.getListDoanhThu(year, account_id);
        return listDoanhThu != null ? listDoanhThu : new ArrayList<>();
    }

    public List<Response_Year> getYears(
            Integer account_id) {
        List<Response_Year> years = billRepository.getDistinctYears(account_id);
        return years.isEmpty() ? Collections.singletonList(new Response_Year(Year.now().getValue())) : years;
    }
}
