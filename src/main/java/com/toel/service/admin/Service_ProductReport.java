package com.toel.service.admin;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

import com.toel.dto.admin.response.Response_ProductReport;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.ProductReportMapper;
import com.toel.model.ProductReport;
import com.toel.repository.ProductReportRepository;
import com.toel.service.Email.EmailService;
import com.toel.service.Email.EmailTemplateType;

@Service
public class Service_ProductReport {
    @Autowired
    ProductReportRepository productReportRepository;
    @Autowired
    ProductReportMapper productReportMapper;
    @Autowired
    EmailService emailService;

    public PageImpl<Response_ProductReport> getAll(String option, int page, int size, Boolean sortBy, String column,
            String key) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<ProductReport> pageProductReport;
        boolean status = (option.equalsIgnoreCase("macdinh") || option.equalsIgnoreCase("chuaphanhoi")) ? false : true;
        if (key == null || key.isBlank()) {
            pageProductReport = productReportRepository.findAllByStatus(status, pageable);
        } else {
            pageProductReport = productReportRepository.findAllByStatusAndTitleContainingOrContentContaining(status,
                    key, key,
                    pageable);
        }
        List<Response_ProductReport> list = pageProductReport.stream()
                .map(productReportMapper::toresponse_ProductReport)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageProductReport.getTotalElements());
    }

    public Response_ProductReport updateStatus(Integer id, String contents) {
        ProductReport entity = productReportRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Report"));
        entity.setStatus(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = entity.getCreateAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = localDate.format(formatter);
        contents = (contents == null || contents.isEmpty()) ? "Chúng tôi đã giải quyết báo cáo của bạn." : contents;
        emailService.push(entity.getAccount().getEmail(), "TOEL - Thông Báo Phản Hồi Báo Cáo",
                EmailTemplateType.PHANHOIREPORT, entity.getAccount().getFullname(), contents, formattedDate,
                entity.getTitle(), entity.getContent());
        return productReportMapper.toresponse_ProductReport(productReportRepository.save(entity));
    }
}
