package com.toel.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.Account;
import com.toel.model.Category;
import com.toel.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

        Page<Product> findAllByIdNotAndCategory(Integer id, Category categories, Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.account = ?1 AND p.category != ?2 AND p.isDelete = false AND p.isActive = true")
        Page<Product> findAllByAccountAndCategoryNot(Account account, Category category, Pageable pageable);

        Page<Product> findAllByNameContainingAndIsActiveTrueAndIsDeleteFalse(String name, Pageable pageable);

        Page<Product> findByCategoryInAndIdIn(List<Category> categories, List<Integer> idProduct, Pageable pageable);

        @Query("SELECT p FROM Product p WHERE ((p.price- ((p.price*p.sale)/100)) between ?1 AND ?2) AND (id IN ?3)")
        Page<Product> findByPriceBetweenAndIdIn(double priceMin, double priceMax, List<Integer> idProduct,
                        Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.isActive = false and p.isDelete=false " +
                        "AND (:price IS NULL OR p.price = :price OR p.sale = :price )" +
                        "AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% " +
                        "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
        Page<Product> selectAllByActiveAndDeleteAndMatchingAttributes(@Param("key") String key,
                        @Param("price") Double price,
                        Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.isActive = true " +
                        "AND (:price IS NULL OR p.price = :price OR p.sale = :price )" +
                        "AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% " +
                        "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
        Page<Product> selectAllByActiveAndMatchingKey(@Param("key") String key, @Param("price") Double price,
                        Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.isActive != false And p.isDelete != true " +
                        "AND (:price IS NULL OR p.price = :price OR p.sale = :price )" +
                        "AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% " +
                        "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
        Page<Product> selectAllMatchingKey(@Param("key") String key, @Param("price") Double price,
                        Pageable pageable);

        @Query("SELECT p FROM Product p where p.account.id = ?1 AND p.isDelete = false " +
                        "AND (?2 IS NULL OR p.name LIKE CONCAT('%', ?2, '%'))")
        Page<Product> findByAccountId(Integer account_id, String search, Pageable pageable);

        @Query("SELECT p FROM Product p where p.account.id = ?1 AND p.isDelete = false ")
        List<Product> findByAccountId(Integer account_id);

        @Query("SELECT p FROM Product p WHERE p.isDelete=false and p.isActive=true and p.id NOT IN (SELECT fl.product.id FROM FlashSaleDetail fl Where fl.flashSale.id =?1)")
        Page<Product> selectAllProductNotInFlashSale(Integer flashSaleId, Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.isDelete=false and p.isActive=true and p.id IN (SELECT fl.product.id FROM FlashSaleDetail fl Where fl.flashSale.id =?1)")
        Page<Product> selectAllProductInFlashSale(Integer flashSaleId, Pageable pageable);

        Page<Product> findAllByIsDeleteAndIsActive(Boolean isDelete, Boolean isActive, Pageable pageable);

        List<Product> findAllByAccount(Account account);

        @Query(value = "SELECT * FROM products WHERE id not IN( SELECT id FROM products WHERE isActive = ?1 and isDelete = ?2)", nativeQuery = true)
        Page<Product> findAllByIsActiveNotAndIsDeleteNot(Boolean isActive, Boolean isDelete, Pageable pageable);

        Page<Product> findByIsActive(boolean isActive, Pageable pageable);

        List<Product> findAllByIsDeleteAndIsActiveAndCreateAtBetween(
                        Boolean isDelete,
                        Boolean isActive,
                        Date dateStart,
                        Date dateEnd);

        @Query("SELECT p FROM Product p WHERE p.isActive = true and p.isDelete=false " +
                        "AND (p.createAt BETWEEN :dateStart AND :dateEnd) " +
                        "AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% " +
                        "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
        List<Product> selectAllMatchingAttributesByDateStartAndDateEnd(@Param("key") String key,
                        @Param("dateStart") Date dateStart,
                        @Param("dateEnd") Date dateEnd);

        // thống kê seller
        @Query("SELECT b.account FROM Product b WHERE b.createAt BETWEEN ?1 AND ?2")
        List<Account> selectAllByProductAndCreateAt(Date dateStart, Date dateEnd);

        // thống kê seller
        @Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN ?1 AND ?2 And b.account.gender = ?3")
        List<Account> selectAllByProductAndGenderFinishAt(Date dateStart, Date dateEnd, Boolean gender);

        @Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN :finishDateStart AND :finishDateEnd " +
                        "AND (:gender IS NULL OR b.account.gender = :gender)" +
                        "AND (b.account.username LIKE %:username% OR b.account.fullname LIKE %:fullname% " +
                        "OR b.account.email LIKE %:email% OR b.account.phone LIKE %:phone%) ")
        List<Account> findAllByProductCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
                        @Param("finishDateStart") Date finishDateStart,
                        @Param("finishDateEnd") Date finishDateEnd,
                        @Param("gender") Boolean gender,
                        @Param("username") String username,
                        @Param("fullname") String fullname,
                        @Param("email") String email,
                        @Param("phone") String phone);

        @Query("SELECT p FROM Product p WHERE p.id NOT IN :idProducts")
        Page<Product> findAllIdNotIn(@Param("idProducts") List<Integer> idProducts, Pageable pageable);

        @Query(value = "SELECT * FROM products p WHERE p.isDelete=false and p.isActive=false and p.createAt < NOW() - INTERVAL 7 DAY", nativeQuery = true)
        List<Product> findAllCreatedBeforeSevenDays();
}
