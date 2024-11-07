package com.toel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Account;
import com.toel.model.Role;
import java.util.Date;

public interface AccountRepository extends JpaRepository<Account, Integer> {

        Page<Account> findAllByRoleAndStatus(Role role, boolean status, Pageable pageable);

        Page<Account> findAllByRoleAndStatusAndGender(Role role, boolean status, boolean gender, Pageable pageable);

        Page<Account> findAllByUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContainingAndStatusAndRole(
                        String username, String fullname, String email, String phone, boolean status, Role role,
                        Pageable pageable);

        @Query("SELECT a FROM Account a WHERE a.gender = ?1 AND a.status = ?2 AND a.role = ?3 " +
                        "AND (a.username LIKE %?4% OR a.fullname LIKE %?5% OR a.email LIKE %?6% OR a.phone LIKE %?7%)")
        Page<Account> findAllByGenderAndStatusAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                        boolean gender, boolean status, Role role, String username, String fullname, String email,
                        String phone, Pageable pageable);

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

        Page<Account> findAllByUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContainingAndRole(
                        String username, String fullname, String email, String phone, Role role, Pageable pageable);

        @Query("SELECT a FROM Account a WHERE a.gender = ?1 AND a.role = ?2 " +
                        "AND (a.username LIKE %?3% OR a.fullname LIKE %?4% OR a.email LIKE %?5% OR a.phone LIKE %?6%)")
        Page<Account> findAllByGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                        boolean gender, Role role, String username, String fullname, String email,
                        String phone, Pageable pageable);

        Account findByUsername(String username);

        boolean existsByEmail(String email);

        Account findByEmail(String email);

        Page<Account> findAllByCreateAtBetweenAndRole(Date dateStart, Date dateEnd, Role role, Pageable pageable);

        Page<Account> findAllByCreateAtBetweenAndRoleAndGender(Date dateStart, Date dateEnd, Role role, boolean gender,
                        Pageable pageable);

        Page<Account> findAllByCreateAtBetweenAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContainingAndRole(
                        Date dateStart, Date dateEnd, String username, String fullname, String email, String phone,
                        Role role, Pageable pageable);

        @Query("SELECT a FROM Account a WHERE a.gender = ?1 AND a.role = ?2 " +
                        "AND (a.username LIKE %?3% OR a.fullname LIKE %?4% OR a.email LIKE %?5% OR a.phone LIKE %?6%) "+
                        "AND a.createAt Between ?7 and ?8")
        Page<Account> findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                        boolean gender, Role role, String username, String fullname, String email,
                        String phone, Date dateStart, Date dateEnd, Pageable pageablle);

}
