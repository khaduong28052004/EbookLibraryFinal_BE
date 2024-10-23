package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
