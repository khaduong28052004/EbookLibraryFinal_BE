package com.toel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.BillDetail;



public interface BillDetailRepository extends JpaRepository<BillDetail, Integer> {

    @Query("SELECT  bd.quantity,  bd.bill.account.id, bd.product.id  FROM BillDetail bd  WHERE bd.bill.id = ?1")
    List<Object[]> getOriginBillsByBillId(Integer billId);
	
}
