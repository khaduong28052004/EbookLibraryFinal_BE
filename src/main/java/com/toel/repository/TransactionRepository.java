package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Transaction;



public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
