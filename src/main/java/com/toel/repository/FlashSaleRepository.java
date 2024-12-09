package com.toel.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.FlashSale;
import java.util.List;

public interface FlashSaleRepository extends JpaRepository<FlashSale, Integer> {
	@Query("SELECT f FROM FlashSale f WHERE (CAST(f.dateStart AS date) BETWEEN :dateStart AND :dateEnd OR CAST(f.dateEnd AS date) BETWEEN :dateStart AND :dateEnd)")
	Page<FlashSale> findAllByDateStartOrDateEnd(LocalDate dateStart, LocalDate dateEnd,
			Pageable pageable);

	Page<FlashSale> findAllByIsDelete(boolean delete, Pageable pageable);

	@Query("SELECT f FROM FlashSale f WHERE f.dateStart <= :date AND :date <= f.dateEnd AND f.isDelete = false")
	FlashSale findFlashSaleNow(@Param("date") LocalDateTime date);

	List<FlashSale> findByIsDelete(boolean delete);

	@Query("SELECT f FROM FlashSale f WHERE FUNCTION('YEAR', f.dateStart) = FUNCTION('YEAR', ?1) "
			+ "AND FUNCTION('MONTH', f.dateStart) = FUNCTION('MONTH', ?1) "
			+ "AND FUNCTION('DAY', f.dateStart) = FUNCTION('DAY', ?1) " + "AND ?1 <= f.dateEnd "
			+ "AND f.isDelete = false")
	Page<FlashSale> findAllByNow(LocalDateTime now, Pageable pageable);

}
