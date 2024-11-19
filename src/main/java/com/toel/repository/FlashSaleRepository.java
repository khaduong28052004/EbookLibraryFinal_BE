package com.toel.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.FlashSale;

public interface FlashSaleRepository extends JpaRepository<FlashSale, Integer> {
	@Query("SELECT f FROM FlashSale f WHERE (CAST(f.dateStart AS date) BETWEEN :dateStart AND :dateEnd OR CAST(f.dateEnd AS date) BETWEEN :dateStart AND :dateEnd) AND f.isDelete = :delete")
	Page<FlashSale> findAllByDateStartOrDateEndAndIsDelete(LocalDate dateStart, LocalDate dateEnd, boolean delete,
			Pageable pageable);

	Page<FlashSale> findAllByIsDelete(boolean delete, Pageable pageable);

	@Query("SELECT f FROM FlashSale f WHERE :date BETWEEN f.dateStart AND f.dateEnd")
	FlashSale findFlashSaleNow(@Param("date") LocalDateTime date);

}
