package com.toel.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.Account;
import com.toel.model.AccountReport;
import java.util.List;

public interface AccountReportRepository extends JpaRepository<AccountReport, Integer> {
        Page<AccountReport> findAllByStatus(boolean status, Pageable pageable);

        Page<AccountReport> findAllByTitleContainingOrContentContainingAndStatus(String title, String content,
                        boolean status, Pageable pageable);

        Page<AccountReport> findAllByTitleContainingOrContentContaining(String title, String content,
                        Pageable pageable);

        Page<AccountReport> findAllByCreateAtBetween(Date dateStart, Date dateEnd, Pageable pageable);

        Page<AccountReport> findAllByCreateAtBetweenAndTitleContainingOrContentContaining(Date dateStart, Date dateEnd,
                        String title, String content,
                        Pageable pageable);

        int countByCreateAtBetween(Date dateStart, Date dateEnd);

        @Query("SELECT a.shop_id FROM AccountReport a WHERE a.createAt BETWEEN ?1 AND ?2")
        Page<Account> selectAllByProductAndCreateAt(Date dateStart, Date dateEnd, Pageable pageable);

        @Query("SELECT a.shop_id FROM AccountReport a WHERE a.createAt BETWEEN ?1 AND ?2 And a.shop_id.gender = ?3")
        Page<Account> selectAllByProductAndGenderFinishAt(Date dateStart, Date dateEnd, Boolean gender,
                        Pageable pageable);

        @Query("SELECT a.shop_id FROM AccountReport a WHERE a.createAt BETWEEN :finishDateStart AND :finishDateEnd " +
                        "AND (:gender IS NULL OR a.shop_id.gender = :gender)" +
                        "AND (a.shop_id.username LIKE %:username% OR a.shop_id.fullname LIKE %:fullname% " +
                        "OR a.shop_id.email LIKE %:email% OR a.shop_id.phone LIKE %:phone%) ")
        Page<Account> findAllByProductCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                        @Param("finishDateStart") Date finishDateStart,
                        @Param("finishDateEnd") Date finishDateEnd,
                        @Param("gender") Boolean gender,
                        @Param("username") String username,
                        @Param("fullname") String fullname,
                        @Param("email") String email,
                        @Param("phone") String phone,
                        Pageable pageable);
}
