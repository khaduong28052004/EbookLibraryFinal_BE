package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    // @Query("SELECT v FROM Voucher v WHERE v.account.id = ?1")
    // Page<Voucher> findAllByIdAccount( Integer idAccount, Pageable pageable);

}
