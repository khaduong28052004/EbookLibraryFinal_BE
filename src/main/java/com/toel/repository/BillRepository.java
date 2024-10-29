package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query("SELECT b FROM Bill b JOIN b.billDetails bd WHERE bd.product.account.id = ?1")
    Page<Bill> findAllByShopId(Integer shopId, Pageable pageable);

}
