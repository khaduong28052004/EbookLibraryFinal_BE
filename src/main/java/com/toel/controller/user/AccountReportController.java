package com.toel.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.model.AccountReport;
import com.toel.model.ProductReport;
import com.toel.service.admin.Service_Account;
import com.toel.service.admin.Service_AccountReport;
import com.toel.service.admin.Service_Product;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class AccountReportController {
    @Autowired
    private Service_AccountReport accountReportService;
    @Autowired
    private Service_Product productReportService;
    @GetMapping("/reports/{accountId}")
    public ResponseEntity<?> getReportsByAccountId(@PathVariable int accountId) {
        List<AccountReport> reports = accountReportService.getReportsByAccountId(accountId);
        return ResponseEntity.ok(reports);
    }

    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody AccountReport report) {
        AccountReport savedReport = accountReportService.saveReport(report);
        return ResponseEntity.ok(savedReport);
    }
// Báo cáo sản phẩm
    @GetMapping("/reports/product/{accountId}")
    public ResponseEntity<?> getReportsByProductId(@PathVariable int accountId) {
        List<ProductReport> reports = productReportService.getReportsByAccountId(accountId);
        return ResponseEntity.ok(reports);
    }
}

