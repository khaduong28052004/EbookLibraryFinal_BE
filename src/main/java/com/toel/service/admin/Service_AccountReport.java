package com.toel.service.admin;

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
public class Service_AccountReport {
    @Autowired
    AccountReportRepository accountReportRepository;
    @Autowired
    AccountReportMapper accountReportMapper;

    public PageImpl<Response_AccountReport> getAll(int page, int size, Boolean sortBy, String column, String key) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<AccountReport> pageProductReport;
        if (key.isBlank() || key == null) {
            pageProductReport = accountReportRepository.findAllByStatus(false, pageable);
        } else {
            pageProductReport = accountReportRepository.findAllByTitleContainingOrContentContainingAndStatus(key, key,
                    false, pageable);
        }
        List<Response_AccountReport> list = pageProductReport.stream()
                .map(accountReportMapper::toResponse_AccountReport)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageProductReport.getTotalElements());
    }

    public Response_AccountReport getId(Integer id) {
        return accountReportMapper.toResponse_AccountReport(accountReportRepository.findById(id).get());
    }
}
