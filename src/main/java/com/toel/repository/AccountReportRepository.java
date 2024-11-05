package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.AccountReport;

public interface AccountReportRepository extends JpaRepository<AccountReport, Integer> {
    Page<AccountReport> findAllByStatus(boolean status, Pageable pageable);

    Page<AccountReport> findAllByTitleContainingOrContentContainingAndStatus(String title, String content,
            boolean status, Pageable pageable);
}
