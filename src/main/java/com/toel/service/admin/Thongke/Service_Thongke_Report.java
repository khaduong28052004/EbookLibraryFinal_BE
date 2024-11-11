package com.toel.service.admin.Thongke;

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

import com.toel.dto.admin.response.Response_AccountReport;
import com.toel.mapper.AccountReportMapper;
import com.toel.model.AccountReport;
import com.toel.repository.AccountReportRepository;

@Service
public class Service_Thongke_Report {
    @Autowired
    AccountReportRepository accountReportRepository;
    @Autowired
    AccountReportMapper accountReportMapper;

    public PageImpl<Response_AccountReport> getAll(int page, int size, Boolean sortBy, String column, String key,
            Date dateStart, Date dateEnd) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<AccountReport> pageAccountReport;
        if (key.isBlank() || key == null) {
            if (dateStart == null || dateEnd == null) {
                pageAccountReport = accountReportRepository.findAll(pageable);
            } else {
                pageAccountReport = accountReportRepository.findAllByCreateAtBetween(dateStart, dateEnd, pageable);
            }
        } else {
            if (dateStart == null || dateEnd == null) {
                pageAccountReport = accountReportRepository.findAllByTitleContainingOrContentContaining(key, key,
                        pageable);
            } else {
                pageAccountReport = accountReportRepository
                        .findAllByCreateAtBetweenAndTitleContainingOrContentContaining(dateStart, dateEnd, key, key,
                                pageable);
            }
        }
        List<Response_AccountReport> list = pageAccountReport.stream()
                .map(accountReportMapper::toResponse_AccountReport)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageAccountReport.getTotalElements());
    }

}
