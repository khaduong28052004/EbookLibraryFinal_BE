package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Account;
import com.toel.model.Role;
import java.util.List;

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

    Account findByUsername(String username);

    boolean existsByEmail(String email);

}
