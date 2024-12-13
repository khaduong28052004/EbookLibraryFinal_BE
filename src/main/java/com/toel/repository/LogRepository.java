package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Account;
import com.toel.model.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {
    Page<Log> findByAccount(Account account, Pageable pageable);

    Page<Log> findByAccountFullname(String fullname, Pageable pageable);

    Page<Log> findByAccountFullnameAndAccount(String accountFullname, Account account, Pageable pageable);
}
