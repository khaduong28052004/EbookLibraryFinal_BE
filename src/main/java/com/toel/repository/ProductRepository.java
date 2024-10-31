package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p where p.account.id = ?1")
    Page<Product> findByAccountId(Integer account_id, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id NOT IN (SELECT fl.product.id FROM FlashSaleDetail fl Where fl.id =?1)")
    Page<Product> selectAllProductNotInFlashSale(Integer flashSaleId,Pageable pageable);


}
