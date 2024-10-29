package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.FlashSale;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

public interface FlashSaleRepository extends JpaRepository<FlashSale, Integer> {
    Page<FlashSale> findAllByDateStartOrDateEnd(LocalDateTime dateStart, LocalDateTime dateEnd, Pageable pageable);
}
