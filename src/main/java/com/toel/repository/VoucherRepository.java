package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Voucher;



public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
 
}
