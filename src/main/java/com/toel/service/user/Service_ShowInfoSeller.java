package com.toel.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.Http;
import com.toel.dto.user.resquest.Request_ReportShop_DTO;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.model.Account;
import com.toel.model.AccountReport;
import com.toel.model.Evalue;
import com.toel.model.ImageEvalue;
import com.toel.repository.AccountRepository;
import com.toel.repository.ReportRepository;
import com.toel.service.firebase.UploadImage;

import jakarta.persistence.EntityNotFoundException;

@Service
public class Service_ShowInfoSeller {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    UploadImage firebaseUploadImages;

    public Map<String, Object> createReportShop(Request_ReportShop_DTO reportDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            validateEntitiesExist(reportDTO);
            createReport(reportDTO);
            response.put("message", "Báo cáo đã được gửi");
            response.put("status", HttpStatus.SC_OK);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("status", HttpStatus.SC_BAD_REQUEST);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("status", HttpStatus.SC_BAD_REQUEST);
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

        System.out.println("accountIdval " + accountId);
        System.out.println("shopIdval " + shopId);
        System.out.println("contentval " + content);
        System.out.println("createAtval " + createAt);
        System.out.println("titleval " + title);

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
            throw new AppException(ErrorCode.OBJECT_SETUP, "Bạn đã báo cáo. Đang trong quá trình xử lý");
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

        // if (reportDTO.getImages() != null) {
        // List<Object[]> imageEvalues = saveImages(reportDTO.getImages(), newReport);
        // newReport.setImages(imageEvalues);
        // imageReportShop.save(newReport);
        // }

        reportRepository.save(newReport);
        return newReport;
    }

    // private List<ImageReportShop> saveImages(MultipartFile[] imageFiles,
    // AccountReport report) {
    // List<ImageReportShop> imageReportShop = new ArrayList<>();

    // for (MultipartFile imageFile : imageReportShop) {
    // if (!imageFile.isEmpty()) {
    // try {
    // String imageFirebaseURL = firebaseUploadImages.uploadFile("report",
    // imageFile);
    // if (imageFirebaseURL == null || imageFirebaseURL.isEmpty()) {
    // throw new AppException(ErrorCode.OBJECT_SETUP,
    // "Failed to upload image to Firebase: " + imageFile.getOriginalFilename());
    // }

    // ImageReportShop imageReport = new ImageReportShop();
    // imageReport.setName(imageFirebaseURL);
    // imageReport.setReport(report);
    // imageEvaluateRepository.saveAndFlush(imageReport);

    // imageReportShop.add(imageReport);
    // } catch (Exception e) {
    // e.printStackTrace();
    // throw new AppException(ErrorCode.OBJECT_SETUP,
    // "Error processing image: " + imageFile.getOriginalFilename(), e);
    // }
    // }
    // }

    // return imageReportShop;
    // }

}
