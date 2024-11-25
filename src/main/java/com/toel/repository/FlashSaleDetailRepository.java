package com.toel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;

public interface FlashSaleDetailRepository extends JpaRepository<FlashSaleDetail, Integer> {
	Page<FlashSaleDetail> findAllByFlashSale(FlashSale flashSale, Pageable pageable);

	@Query("SELECT f FROM FlashSaleDetail f WHERE f.flashSale = ?1 AND f.product.isDelete = false AND f.product.isActive = true AND f.product.account.id !=?2")
	Page<FlashSaleDetail> findAllByFlashSale(FlashSale flashSale, Integer idShop, Pageable pageable);

	@Query("SELECT f FROM FlashSaleDetail f WHERE f.flashSale = ?1")
	List<FlashSaleDetail> findAllByFlashSale(FlashSale flashSale);
}
