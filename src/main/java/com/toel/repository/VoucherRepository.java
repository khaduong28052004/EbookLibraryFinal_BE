package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    @Query("SELECT v FROM Voucher v WHERE v.account.id = ?1")
    Page<Voucher> findAllByIdAccount(Integer idAccount, Pageable pageable);

    @Query("SELECT v FROM Voucher v WHERE LOWER(v.account.role.name) LIKE 'adminv1' OR LOWER(v.account.role.name) LIKE 'admin' AND (:key IS NULL OR v.name = :key) ")
    Page<Voucher> findAllByAdmin(@Param("key") String key,Pageable pageable);

}
