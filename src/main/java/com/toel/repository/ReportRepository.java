package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.AccountReport;

public interface ReportRepository extends JpaRepository<AccountReport, Integer> {

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM accountreports ar "
            + "WHERE ar.account_id = :accountId AND ar.shop_id = :shopId AND ar.status=1)"
            + "THEN 1 ELSE 0 END AS isReported ", nativeQuery = true)
    Integer isReported(@Param("accountId") Integer accountId, @Param("shopId") Integer shopId);

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM accountreports ar "
    + "WHERE ar.account_id = :accountId AND ar.shop_id = :shopId AND ar.status=0)"
    + "THEN 1 ELSE 0 END AS isReported ", nativeQuery = true)
Integer waitAfterReport(@Param("accountId") Integer accountId, @Param("shopId") Integer shopId);

}
