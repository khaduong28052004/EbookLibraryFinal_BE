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

import com.toel.dto.admin.response.Response_AccountReport;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.AccountReportMapper;
import com.toel.model.AccountReport;
import com.toel.repository.AccountReportRepository;
import com.toel.service.Email.EmailService;
import com.toel.service.Email.EmailTemplateType;

@Service
public class Service_AccountReport {
    @Autowired
    AccountReportRepository accountReportRepository;
    @Autowired
    AccountReportMapper accountReportMapper;
// <<<<<<< HEAD

// Report
    public List<AccountReport> getReportsByAccountId(int accountId) {
        return accountReportRepository.findByAccountId(accountId);
    }

    public AccountReport saveReport(AccountReport report) {
        return accountReportRepository.save(report);
    }
 
    @Autowired
    EmailService emailService;

    public PageImpl<Response_AccountReport> getAll(String option, int page, int size, Boolean sortBy, String column,
            String key) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<AccountReport> pageProductReport;
        boolean status = (option.equalsIgnoreCase("macdinh") || option.equalsIgnoreCase("chuaphanhoi")) ? false : true;
        if (key == null || key.isBlank()) {
            pageProductReport = accountReportRepository.findAllByStatus(status, pageable);
        } else {
            pageProductReport = accountReportRepository.findAllByStatusAndTitleContainingOrContentContaining(status,
                    key, key,
                    pageable);
        }
        List<Response_AccountReport> list = pageProductReport.stream()
                .map(accountReportMapper::toResponse_AccountReport)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageProductReport.getTotalElements());
    }

    public Response_AccountReport getId(Integer id) {
        return accountReportMapper.toResponse_AccountReport(accountReportRepository.findById(id).get());
    }

    public Response_AccountReport updateStatus(int id, String contents) {
        AccountReport entity = accountReportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Report"));
        entity.setStatus(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = entity.getCreateAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = localDate.format(formatter);
        contents = (contents == null || contents.isEmpty()) ? "Chúng tôi đã giải quyết báo cáo của bạn." : contents;
        emailService.push(entity.getAccount().getEmail(), "TOEL - Thông Báo Phản Hồi Báo Cáo",
                EmailTemplateType.PHANHOIREPORT, entity.getAccount().getFullname(), contents, formattedDate,
                entity.getTitle(), entity.getContent());
        return accountReportMapper.toResponse_AccountReport(accountReportRepository.save(entity));
    }
}
