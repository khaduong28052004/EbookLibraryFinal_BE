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
    List<Response_DoanhSo> listDoanhSo = billRepository.getListDoanhSo(year, account_id);
    
    // Tạo một danh sách với 12 phần tử mặc định là 0
    List<Response_DoanhSo> defaultList = IntStream.range(0, 12)
            .mapToObj(i -> new Response_DoanhSo(0.0, i + 1)) // Giả định Response_DoanhSo có constructor cho doanh số và tháng
            .collect(Collectors.toList());
    
    // Nếu danh sách không có dữ liệu, trả về danh sách mặc định
    if (listDoanhSo == null || listDoanhSo.isEmpty()) {
        return defaultList;
    }

    // Nếu có dữ liệu, cập nhật doanh số cho từng tháng
    for (Response_DoanhSo doanhSo : listDoanhSo) {
        // Giả định rằng Response_DoanhSo có phương thức getMonth() trả về số tháng
        int monthIndex = doanhSo.getMonth() - 1; // Lấy chỉ số tháng (0-11)
        defaultList.set(monthIndex, doanhSo); // Cập nhật vào danh sách mặc định
    }

    return defaultList; // Trả về danh sách đã được cập nhật
}

public List<Response_DoanhThu> getListDoanhThu(Integer year, Integer account_id) {
    List<Response_DoanhThu> listDoanhThu = billRepository.getListDoanhThu(year, account_id);
    
    // Tạo một danh sách với 12 phần tử mặc định là 0
    List<Response_DoanhThu> defaultList = IntStream.range(0, 12)
            .mapToObj(i -> new Response_DoanhThu(0.0, i + 1)) // Giả định Response_DoanhThu có constructor cho doanh thu và tháng
            .collect(Collectors.toList());
    
    // Nếu danh sách không có dữ liệu, trả về danh sách mặc định
    if (listDoanhThu == null || listDoanhThu.isEmpty()) {
        return defaultList;
    }

    // Nếu có dữ liệu, cập nhật doanh thu cho từng tháng
    for (Response_DoanhThu doanhThu : listDoanhThu) {
        // Giả định rằng Response_DoanhThu có phương thức getMonth() trả về số tháng
        int monthIndex = doanhThu.getMonth() - 1; // Lấy chỉ số tháng (0-11)
        defaultList.set(monthIndex, doanhThu); // Cập nhật vào danh sách mặc định
    }

    return defaultList; // Trả về danh sách đã được cập nhật
}

    public List<Response_Year> getYears(
            Integer account_id) {
        List<Response_Year> years = billRepository.getDistinctYears(account_id);
        return years.isEmpty() ? Collections.singletonList(new Response_Year(Year.now().getValue())) : years;
    }
}
