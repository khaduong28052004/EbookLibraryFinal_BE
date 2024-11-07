package com.toel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;

public interface FlashSaleDetailRepository extends JpaRepository<FlashSaleDetail, Integer> {
    Page<FlashSaleDetail> findAllByFlashSale(FlashSale flashSale,Pageable pageable);
    
	List<FlashSaleDetail> findAllByFlashSale(FlashSale flashSale);
}
