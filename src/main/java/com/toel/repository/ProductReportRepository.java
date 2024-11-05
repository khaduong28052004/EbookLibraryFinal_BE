package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.ProductReport;
import java.util.List;

public interface ProductReportRepository extends JpaRepository<ProductReport, Integer> {
    Page<ProductReport> findAllByStatus(boolean status, Pageable pageable);

    Page<ProductReport> findAllByTitleContainingOrContentContainingAndStatus(String title, String content,
            boolean status, Pageable pageable);
}
