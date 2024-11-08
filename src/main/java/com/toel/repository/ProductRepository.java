package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.Product;
import com.toel.model.Account;
import java.util.List;
import java.util.Date;

public interface ProductRepository extends JpaRepository<Product, Integer> {

        @Query("SELECT p FROM Product p WHERE p.isActive = false and p.isDelete=false " +
                        "AND (:price IS NULL OR p.price = :price OR p.sale = :price )" +
                        "AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% " +
                        "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
        Page<Product> selectAllByActiveAndDeleteAndMatchingAttributes(@Param("key") String key,
                        @Param("price") Double price,
                        Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.isActive = true and p.isDelete=false " +
                        "AND (:price IS NULL OR p.price = :price OR p.sale = :price )" +
                        "AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% " +
                        "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
        Page<Product> selectAllByActiveAndMatchingKey(@Param("key") String key, @Param("price") Double price,
                        Pageable pageable);

        @Query("SELECT p FROM Product p where p.account.id = ?1 AND p.isDelete = false " +
                        "AND (?2 IS NULL OR p.name LIKE CONCAT('%', ?2, '%'))")
        Page<Product> findByAccountId(Integer account_id, String search, Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.isDelete=false and p.id NOT IN (SELECT fl.product.id FROM FlashSaleDetail fl Where fl.id =?1)")
        Page<Product> selectAllProductNotInFlashSale(Integer flashSaleId, Pageable pageable);

        Page<Product> findAllByIsDeleteAndIsActive(Boolean isDelete, Boolean isActive, Pageable pageable);

        List<Product> findAllByAccount(Account account);

        Page<Product> findByIsActive(boolean isActive, Pageable pageable);

        Page<Product> findAllByIsDeleteAndIsActiveAndCreateAtBetween(
                        Boolean isDelete,
                        Boolean isActive,
                        Date dateStart,
                        Date dateEnd,
                        Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.isActive = true and p.isDelete=false " +
                        "AND (p.createAt BETWEEN :dateStart AND :dateEnd) " +
                        "AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% " +
                        "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
        Page<Product> selectAllMatchingAttributesByDateStartAndDateEnd(@Param("key") String key,
                        @Param("dateStart") Date dateStart,
                        @Param("dateEnd") Date dateEnd,
                        Pageable pageable);

        @Query("SELECT b.account FROM Product b WHERE b.createAt BETWEEN ?1 AND ?2")
        Page<Account> selectAllByProductAndCreateAt(Date dateStart, Date dateEnd, Pageable pageable);

        @Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN ?1 AND ?2 And b.account.gender = ?3")
        Page<Account> selectAllByProductAndGenderFinishAt(Date dateStart, Date dateEnd, Boolean gender,
                        Pageable pageable);

        @Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN :finishDateStart AND :finishDateEnd " +
                        "AND (:gender IS NULL OR b.account.gender = :gender)" +
                        "AND (b.account.username LIKE %:username% OR b.account.fullname LIKE %:fullname% " +
                        "OR b.account.email LIKE %:email% OR b.account.phone LIKE %:phone%) ")
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
