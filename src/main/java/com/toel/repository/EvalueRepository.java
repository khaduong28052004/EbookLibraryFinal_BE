package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Evalue;

public interface EvalueRepository extends JpaRepository<Evalue, Integer> {
    @Query("SELECT e FROM Evalue e WHERE e.product.account.id = ?1")
    Page<Evalue> findByAccountId(Integer account_id, Pageable pageable);

}
