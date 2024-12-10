package com.toel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Account;
import com.toel.model.Product;
import com.toel.model.ProductReport;

public interface ProductReportRepository extends JpaRepository<ProductReport, Integer> {

	List<ProductReport> findByAccountAndProduct(Account user, Product product);

}
