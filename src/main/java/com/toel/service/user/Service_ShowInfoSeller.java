package com.toel.service.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.user.resquest.Request_ReportShop_DTO;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.model.Account;
import com.toel.model.AccountReport;
import com.toel.repository.AccountRepository;
import com.toel.repository.ReportRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class Service_ShowInfoSeller {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private ReportRepository reportRepository;

    public Map<String, Object> createReportShop(Request_ReportShop_DTO reportDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            validateEntitiesExist(reportDTO);
            createReport(reportDTO);
            response.put("message", "Báo cáo đã được gửi");
            response.put("status", "successfully");
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("status", "fail");
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("status", "error");
            e.printStackTrace();
        }
        return response;
    }

    private void validateEntitiesExist(Request_ReportShop_DTO reportDTO) throws EntityNotFoundException {
        Integer accountId = reportDTO.getAccountId() == null ? 0 : reportDTO.getAccountId();
        Integer shopId = reportDTO.getShopId() == null ? null : reportDTO.getShopId();
        String content = reportDTO.getContent() == null ? "" : reportDTO.getContent();
        Date createAt = reportDTO.getCreateAt() == null ? null : reportDTO.getCreateAt();
        String title = reportDTO.getTitle() == null ? "" : reportDTO.getTitle();

        if (!accountRepository.existsById(accountId) || accountId == null) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Tài khoản đang sử dụng không tồn tại");
        }
        if (!accountRepository.existsById(shopId) || shopId == null) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Tài khoản cửa hàng không tồn tại");
        }
        if (content.trim().isBlank() || content == "") {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Cần điền nội dung báo cáo");
        }
        System.out.println("createAt" + reportDTO.getCreateAt());
        if (createAt == null) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Thiếu trường createAt");
        }
        if (title == null) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Thiếu trường title");
        }
        if (reportRepository.waitAfterReport(accountId, shopId) == 1) {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Bạn đã báo cáo shop. Đang trong quá trình xử lý");
        }
    }

    private AccountReport createReport(Request_ReportShop_DTO reportDTO) {
        AccountReport newReport = new AccountReport();
        Account account = accountRepository.findById(reportDTO.getAccountId()).get();
        Account shop = accountRepository.findById(reportDTO.getShopId()).get();
        newReport.setAccount(account);
        newReport.setShop(shop);
        newReport.setContent(reportDTO.getContent());
        System.out.println("reportDTO.getCreateAt()" + reportDTO.getCreateAt());
        newReport.setCreateAt(reportDTO.getCreateAt());
        newReport.setStatus(reportDTO.isStatus());
        newReport.setTitle(reportDTO.getTitle());
        reportRepository.save(newReport);
        return newReport;
    }

}
