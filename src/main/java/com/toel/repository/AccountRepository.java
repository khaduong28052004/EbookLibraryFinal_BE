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
    // account
    Page<Account> findAllByRole(Role role, Pageable pageable);

    // thống kê khách hàng
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

    // account
    Page<Account> findAllByRoleAndGender(Role role, boolean gender, Pageable pageable);

    // account
    @Query("SELECT a FROM Account a WHERE (:gender IS NULL OR a.gender = :gender) " +
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

    // Account findByEmail(String email);

    boolean existsByUsername(String username);

    Account findByIdAndPassword(Integer id, String Password);

    boolean existsByNumberId(String cccdNumber);

    // thống kê khách hàng
    List<Account> findAllByRoleAndGender(Role role, boolean gender);

    // thống kê khách hàng
    @Query("SELECT a FROM Account a WHERE (:gender IS NULL OR a.gender = :gender) " +
            "AND a.role = :role " +
            "AND (a.username LIKE %:username% OR a.fullname LIKE %:fullname% OR a.shopName LIKE %:shopName% " +
            "OR a.email LIKE %:email% OR a.phone LIKE %:phone%)")
    List<Account> findAllByGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
            @Param("gender") Boolean gender,
            @Param("role") Role role,
            @Param("username") String username,
            @Param("fullname") String fullname,
            @Param("shopName") String shopName,
            @Param("email") String email,
            @Param("phone") String phone);

    // thống kê khách hàng
    List<Account> findAllByCreateAtBetweenAndRole(Date dateStart, Date dateEnd, Role role);

    // thống kê khách hàng
    List<Account> findAllByCreateAtBetweenAndRoleAndGender(Date dateStart, Date dateEnd, Role role, boolean gender);

    // thống kê khách hàng
    @Query("SELECT a FROM Account a WHERE (:gender IS NULL OR a.gender = :gender) " +
            "AND a.role = :role " +
            "AND (a.username LIKE %:username% OR a.fullname LIKE %:fullname% " +
            "OR a.email LIKE %:email% OR a.phone LIKE %:phone%) " +
            "AND a.createAt BETWEEN :dateStart AND :dateEnd")
    List<Account> findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
            @Param("gender") Boolean gender,
            @Param("role") Role role,
            @Param("username") String username,
            @Param("fullname") String fullname,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("dateStart") Date dateStart,
            @Param("dateEnd") Date dateEnd);

    // check - nhanvien
    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByPhoneIgnoreCase(String phone);

    boolean existsByEmailIgnoreCase(String email);

    @Query(value = "SELECT * FROM accounts a WHERE a.role_id = 4 AND a.numberId IS NOT NULL AND a.createAtSeller < NOW() - INTERVAL 7 DAY", nativeQuery = true)
    List<Account> listAccountBeforeSevenDays();

    List<Account> findAllByStatusAndRole(boolean status, Role role);

    @Query("SELECT a FROM Account a WHERE a.role IN :roles")
    Page<Account> findAllByRolesIn(@Param("roles") List<Role> roles, Pageable pageable);

    @Query("SELECT a FROM Account a WHERE a.role IN :roles AND a.gender = :gender")
    Page<Account> findAllByRolesInAndGender(
            @Param("roles") List<Role> roles, @Param("gender") Boolean gender, Pageable pageable);

    @Query("""
            SELECT a FROM Account a
            WHERE a.role IN :roles
            AND (a.gender = :gender OR :gender IS NULL)
            AND (a.username LIKE %:search%
                OR a.fullname LIKE %:search%
                OR a.email LIKE %:search%
                OR a.phone LIKE %:search%)
            """)
    Page<Account> findAllByRolesInAndSearch(
            @Param("roles") List<Role> roles,
            @Param("gender") Boolean gender,
            @Param("search") String search,
            Pageable pageable);

    @Query("SELECT a FROM Account a  WHERE  a.id = :accountId")
    Account findAccountById(@Param("accountId") Integer accountId);

    // @Query("SELECT a FROM Account a WHERE a.role IN :roles AND a.gender =
    // :gender")
    // Optional<Account> findByBillOptional(Bill bill);
    Account findByEmail(String email);

    List<Account> findByRoleAndStatus(Role role, boolean status);

    @Query("SELECT a FROM Account a WHERE a.role IN :roles and a.status = :status")
    List<Account> selectAllByRolesIn(@Param("roles") List<Role> roles, @Param("status") Boolean status);

    // seller
    List<Account> findAllByCreateAtSellerBetweenAndRole(Date dateStart, Date dateEnd, Role role);

    List<Account> findAllByCreateAtSellerBetweenAndRoleAndGender(Date dateStart, Date dateEnd, Role role,
            boolean gender);

    @Query("SELECT a FROM Account a WHERE (:gender IS NULL OR a.gender = :gender) " +
            "AND a.role = :role " +
            "AND (a.username LIKE %:username% OR a.fullname LIKE %:fullname% OR a.shopName LIKE %:shopName% " +
            "OR a.email LIKE %:email% OR a.phone LIKE %:phone%) " +
            "AND a.createAtSeller BETWEEN :dateStart AND :dateEnd")
    List<Account> findAllByCreateAtSellerBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
            @Param("gender") Boolean gender,
            @Param("role") Role role,
            @Param("username") String username,
            @Param("fullname") String fullname,
            @Param("email") String email,
            @Param("shopName") String shopName,
            @Param("phone") String phone,
            @Param("dateStart") Date dateStart,
            @Param("dateEnd") Date dateEnd);

    @Query("SELECT count(b) FROM Bill b WHERE b.orderStatus.id = 6 AND FUNCTION('DATE', b.updateAt) = CURRENT_DATE and b.account.id = :account")
    Integer countBillHuy(@Param("account") Integer account);
}
