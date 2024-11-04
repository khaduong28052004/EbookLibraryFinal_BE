package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.Product;
import com.toel.model.Account;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
       // @Query("SELECT p FROM Product p WHERE p.isActive=false AND p.isDelete=false
       // AND (:price IS NULL OR p.price = :price) AND (:sale IS NULL OR p.sale =
       // :sale) AND (:name IS NULL OR p.name LIKE %:name%) AND (:introduce IS NULL OR
       // p.introduce LIKE %:introduce%) AND (:writerName IS NULL OR p.writerName LIKE
       // %:writerName%) AND (:publishingCompany IS NULL OR p.publishingCompany LIKE
       // %:publishingCompany%)")
       // Page<Product> selectAllByActiveAndDeleteAndMatchingAttributes(
       // @Param("price") Double price,
       // @Param("sale") Double sale,
       // @Param("name") String name,
       // @Param("introduce") String introduce,
       // @Param("writerName") String writerName,
       // @Param("publishingCompany") String publishingCompany,
       // Pageable pageable);

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

       @Query("SELECT p FROM Product p where p.account.id = ?1")
       Page<Product> findByAccountId(Integer account_id, Pageable pageable);

       @Query("SELECT p FROM Product p WHERE p.isDelete=false and p.id NOT IN (SELECT fl.product.id FROM FlashSaleDetail fl Where fl.id =?1)")
       Page<Product> selectAllProductNotInFlashSale(Integer flashSaleId, Pageable pageable);

       Page<Product> findAllByIsDeleteAndIsActive(Boolean isDelete, Boolean isActive, Pageable pageable);

       List<Product> findAllByAccount(Account account);

       Page<Product> findByIsActive(boolean isActive, Pageable pageable);

}
