package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.Account;
import com.toel.model.Evalue;

import java.util.Date;
import java.util.List;
import com.toel.model.Product;

public interface EvalueRepository extends JpaRepository<Evalue, Integer> {

	@Query("SELECT e FROM Evalue e WHERE e.product.account.id = ?1 AND e.idParent = 0 AND e.id NOT IN (SELECT e2.idParent FROM Evalue e2 WHERE e2.idParent IS NOT NULL)")
	Page<Evalue> findByAccountId(Integer account_id, Pageable pageable);

	@Query(value = "SELECT * FROM evalues e WHERE product_id = :productId AND account_id = :accountId  AND bill_id = :billId LIMIT 1", nativeQuery = true)
	Evalue findByProductIdAndAccountId(@Param("accountId") Integer accountId, @Param("productId") Integer productId,
			@Param("billId") Integer billId);

	@Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM evalues WHERE evalues.bill_id = :billDetailId) THEN 1 ELSE 0 END AS isEvaluated ", nativeQuery = true)
	Integer isEvaluate(@Param("billDetailId") Integer isEvaluate);

	@Query("SELECT COALESCE(AVG(e.star),0) FROM Evalue e WHERE e.product.account.id = :accountId")
	Double calculateAverageStarByAccountId(@Param("accountId") Integer accountId);

	@Query("SELECT COALESCE(AVG(e.star),0) FROM Evalue e WHERE e.product.id = :productId")
	Double calculateAverageStarByProduct(@Param("productId") Integer productId);

	List<Evalue> findAllByProduct(Product product);

	Integer countByProduct(Product product);

	@Query("SELECT p FROM Product p WHERE p.id IN (SELECT DISTINCT e.product.id FROM Evalue e WHERE e.createAt BETWEEN :dateStart AND :dateEnd )")
	Page<Product> sellectAll(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.id IN (SELECT DISTINCT e.product.id FROM Evalue e WHERE e.createAt BETWEEN :dateStart AND :dateEnd AND (:key iS NULL OR e.product.name LIKE %:key% OR e.product.introduce LIKE %:key% OR e.product.writerName LIKE %:key% OR e.product.publishingCompany LIKE %:key%))")
	Page<Product> sellectAllByCreateAt(@Param("key") String key, @Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, Pageable pageable);

	@Query("SELECT COALESCE(AVG(e.star),0) FROM Evalue e WHERE e.account = :account")
	Double calculateAverageStarByKhachHang(@Param("account") Account account);
}
