package com.toel.service.admin;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.Response_ProductReport;
import com.toel.mapper.ProductReportMapper;
import com.toel.model.ProductReport;
import com.toel.repository.ProductReportRepository;

@Service
public class Service_ProductReport {
    @Autowired
    ProductReportRepository productReportRepository;
    @Autowired
    ProductReportMapper productReportMapper;

    public PageImpl<Response_ProductReport> getAll(int page, int size, Boolean sortBy, String column, String key) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<ProductReport> pageProductReport;
        if (key.isBlank() || key == null) {
            pageProductReport = productReportRepository.findAllByStatus(false, pageable);
        } else {
            pageProductReport = productReportRepository.findAllByTitleContainingOrContentContainingAndStatus(key, key,
                    false, pageable);
        }
        List<Response_ProductReport> list = pageProductReport.stream()
                .map(productReportMapper::toResponse_ProductReport)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageProductReport.getTotalElements());
    }

    public Response_ProductReport getId(Integer id) {
        return productReportMapper.toResponse_ProductReport(productReportRepository.findById(id).get());
    }
}
