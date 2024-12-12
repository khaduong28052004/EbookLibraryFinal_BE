package com.toel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.Account;
import com.toel.model.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {
    Page<Log> findByAccount(Account account, Pageable pageable);

    // Page<Log> findByAccountFullname(String fullname, Pageable pageable);

    Page<Log> findByAccountFullnameAndAccount(String accountFullname, Account account, Pageable pageable);

    @Query("SELECT l FROM Log l WHERE l.account.role.name NOT IN :roles")
    Page<Log> selectByAccountRole(@Param("roles") List<String> roles, Pageable pageable);

    @Query("SELECT l FROM Log l WHERE l.account.role.name NOT IN :roles and l.account.fullname LIKE %:key%")
    Page<Log> selectByAccountRoleAndFullName(@Param("roles") List<String> roles, @Param("key") String key,
            Pageable pageable);

    @Query(value = "SELECT l.id FROM logs l WHERE l.timestamps < NOW() - INTERVAL 7 DAY", nativeQuery = true)
    List<Integer> selectAllCreatedBeforeSevenDays();
}
