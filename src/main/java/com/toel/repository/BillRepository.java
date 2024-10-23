package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Bill;


public interface BillRepository extends JpaRepository<Bill, Integer> {




}
