package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.Evalue;
import java.util.List;
import com.toel.model.Product;
import com.toel.model.Account;



public interface EvalueRepository extends JpaRepository<Evalue, Integer> {

    @Query("SELECT e FROM Evalue e WHERE e.product.account.id = ?1 AND e.idParent = 0")
    Page<Evalue> findByAccountId(Integer account_id, Pageable pageable);

    @Query(value = "SELECT * FROM evalues e WHERE product_id = :productId AND account_id = :accountId  AND bill_id = :billId LIMIT 1", nativeQuery = true)
    Evalue findByProductIdAndAccountId(@Param("accountId") Integer accountId, @Param("productId") Integer productId,
            @Param("billId") Integer billId);

    @Query("SELECT AVG(e.star) FROM Evalue e WHERE e.product.account.id = :accountId")
    Double calculateAverageStarByAccountId(@Param("accountId") Integer accountId);

    @Query("SELECT AVG(e.star) FROM Evalue e WHERE e.product.id = :productId")
    Double calculateAverageStarByProduct(@Param("productId") Integer productId);

    List<Evalue> findAllByProduct(Product product);

}
