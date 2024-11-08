package com.toel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Account;
import com.toel.model.Role;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.Date;

public interface AccountRepository extends JpaRepository<Account, Integer> {

        Page<Account> findAllByRoleAndStatus(Role role, boolean status, Pageable pageable);

        Page<Account> findAllByRoleAndStatusAndGender(Role role, boolean status, boolean gender, Pageable pageable);

        @Query("SELECT a FROM Account a WHERE (:gender IS NULL OR a.gender = :gender) " +
                        "AND a.status = :status " +
                        "AND a.role = :role " +
                        "AND (a.username LIKE %:username% OR a.fullname LIKE %:fullname% " +
                        "OR a.email LIKE %:email% OR a.phone LIKE %:phone%)")
        Page<Account> findAllByGenderAndStatusAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                        @Param("gender") Boolean gender,
                        @Param("status") boolean status,
                        @Param("role") Role role,
                        @Param("username") String username,
                        @Param("fullname") String fullname,
                        @Param("email") String email,
                        @Param("phone") String phone,
                        Pageable pageable);

        Page<Account> findAllByRole(Role role, Pageable pageable);

        List<Account> findAllByRole(Role role);

        Page<Account> findAllByRoleAndStatusAndNumberIdIsNotNull(Role role, boolean status, Pageable pageable);

        Page<Account> findAllByRoleAndStatusAndGenderAndNumberIdIsNotNull(Role role, boolean status, boolean gender,
                        Pageable pageable);

        Page<Account> findAllByUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContainingAndStatusAndRoleAndNumberIdIsNotNull(
                        String username, String fullname, String email, String phone, boolean status, Role role,
                        Pageable pageable);

        @Query("SELECT a FROM Account a WHERE a.gender = ?1 AND a.status = ?2 AND a.role = ?3 AND a.numberId IS NOT NULL "
                        +
                        "AND (a.username LIKE %?4% OR a.fullname LIKE %?5% OR a.email LIKE %?6% OR a.phone LIKE %?7%)")
        Page<Account> findAllByGenderAndStatusAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContainingAndNumberIdIsNotNull(
                        boolean gender, boolean status, Role role, String username, String fullname, String email,
                        String phone, Pageable pageable);

        Page<Account> findAllByRoleAndGender(Role role, boolean gender, Pageable pageable);

        @Query("SELECT a FROM Account a WHERE a.status=true and (:gender IS NULL OR a.gender = :gender) " +
                        "AND a.role = :role " +
                        "AND (a.username LIKE %:username% OR a.fullname LIKE %:fullname% " +
                        "OR a.email LIKE %:email% OR a.phone LIKE %:phone%)")
        Page<Account> findAllByGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                        @Param("gender") Boolean gender,
                        @Param("role") Role role,
                        @Param("username") String username,
                        @Param("fullname") String fullname,
                        @Param("email") String email,
                        @Param("phone") String phone,
                        Pageable pageable);

        Account findByUsername(String username);

        boolean existsByEmail(String email);

        Account findByEmail(String email);

        Page<Account> findAllByCreateAtBetweenAndRole(Date dateStart, Date dateEnd, Role role, Pageable pageable);

        Page<Account> findAllByCreateAtBetweenAndRoleAndGender(Date dateStart, Date dateEnd, Role role, boolean gender,
                        Pageable pageable);

        @Query("SELECT a FROM Account a WHERE a.status=true and (:gender IS NULL OR a.gender = :gender) " +
                        "AND a.role = :role " +
                        "AND (a.username LIKE %:username% OR a.fullname LIKE %:fullname% " +
                        "OR a.email LIKE %:email% OR a.phone LIKE %:phone%) " +
                        "AND a.createAt BETWEEN :dateStart AND :dateEnd")
        Page<Account> findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                        @Param("gender") Boolean gender,
                        @Param("role") Role role,
                        @Param("username") String username,
                        @Param("fullname") String fullname,
                        @Param("email") String email,
                        @Param("phone") String phone,
                        @Param("dateStart") Date dateStart,
                        @Param("dateEnd") Date dateEnd,
                        Pageable pageable);

        boolean existsByNumberId(String cccdNumber);
}
