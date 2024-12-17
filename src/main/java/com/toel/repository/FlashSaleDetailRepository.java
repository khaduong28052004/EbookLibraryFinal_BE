package com.toel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;

public interface FlashSaleDetailRepository extends JpaRepository<FlashSaleDetail, Integer> {
	@Query("SELECT f FROM FlashSaleDetail f " +
			"WHERE f.flashSale = :flashSale " +
			"AND (:search IS NULL OR LOWER(f.product.name) LIKE CONCAT('%', LOWER(:search), '%'))")
	Page<FlashSaleDetail> selectAllByFlashSale(@Param("flashSale") FlashSale flashSale,
			@Param("search") String search,
			Pageable pageable);

	@Query("SELECT f FROM FlashSaleDetail f WHERE f.flashSale = ?1 AND f.quantity>0 AND f.product.isDelete = false AND f.product.isActive = true AND f.product.account.id != ?2 AND f.product.account.status = true")
	Page<FlashSaleDetail> findAllByFlashSale(FlashSale flashSale, Integer idShop, Pageable pageable);

	@Query("SELECT f FROM FlashSaleDetail f WHERE f.flashSale = ?1 AND f.quantity>0 AND f.flashSale.isDelete = false AND f.product.isActive = true AND f.product.isDelete = false AND f.product.account.status = true")
	List<FlashSaleDetail> findAllByFlashSale(FlashSale flashSale);

}
