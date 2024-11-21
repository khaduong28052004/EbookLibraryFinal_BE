package com.toel.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toel.model.DiscountRate;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRateRepository extends JpaRepository<DiscountRate, Integer> {
	@Query(value = "SELECT * FROM discountrates WHERE DATE(discountrates.dateDelete) = :dateDelete OR DATE(discountrates.dateInsert) = :dateCreate OR DATE(discountrates.dateStart) = :dateStart", nativeQuery = true)
	Page<DiscountRate> findAllByDateDeleteOrDateCreateOrDateStart(@Param("dateDelete") LocalDateTime dateDelete,
			@Param("dateCreate") LocalDateTime dateCreate, @Param("dateStart") LocalDateTime dateStart,
			Pageable pageable);

	List<DiscountRate> findAllBydateDeleteIsNull();

	@Query("SELECT dr.discount FROM DiscountRate dr ORDER BY dr.id DESC")
	Integer getChietKhau();

	@Query("SELECT r FROM DiscountRate r WHERE ?1 < r.dateStart AND ?1 != r.dateDelete")
	DiscountRate find(LocalDateTime now);

	Optional<DiscountRate> findTopByOrderByIdDesc();
}
