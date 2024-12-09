package com.toel.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.ProductReport;

public interface ProductReportRepository extends JpaRepository<ProductReport, Integer> {
    // Report
    Page<ProductReport> findAllByStatus(boolean status, Pageable pageable);

    Page<ProductReport> findAllByStatusAndTitleContainingOrContentContaining(boolean status, String title,
            String content, Pageable pageable);

    Page<ProductReport> findAllByTitleContainingOrContentContaining(String title, String content,
            Pageable pageable);

    Page<ProductReport> findAllByCreateAtBetween(Date dateStart, Date dateEnd, Pageable pageable);

    Page<ProductReport> findAllByCreateAtBetweenAndTitleContainingOrContentContaining(Date dateStart, Date dateEnd,
            String title, String content,
            Pageable pageable);

}