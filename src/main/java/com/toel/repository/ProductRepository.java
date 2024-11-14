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

	@Query("SELECT p FROM Product p WHERE p.isActive = false and p.isDelete=false "
			+ "AND (:price IS NULL OR p.price = :price OR p.sale = :price )"
			+ "AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% "
			+ "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
	Page<Product> selectAllByActiveAndDeleteAndMatchingAttributes(@Param("key") String key,
			@Param("price") Double price, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isActive = true and p.isDelete=false "
			+ "AND (:price IS NULL OR p.price = :price OR p.sale = :price )"
			+ "AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% "
			+ "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
	Page<Product> selectAllByActiveAndMatchingKey(@Param("key") String key, @Param("price") Double price,
			Pageable pageable);

	@Query("SELECT p FROM Product p where p.account.id = ?1 AND p.isDelete = false "
			+ "AND (?2 IS NULL OR p.name LIKE CONCAT('%', ?2, '%'))")
	Page<Product> findByAccountId(Integer account_id, String search, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isDelete=false and p.id NOT IN (SELECT fl.product.id FROM FlashSaleDetail fl Where fl.id =?1)")
	Page<Product> selectAllProductNotInFlashSale(Integer flashSaleId, Pageable pageable);

	Page<Product> findAllByIsDeleteAndIsActive(Boolean isDelete, Boolean isActive, Pageable pageable);

	List<Product> findAllByAccount(Account account);

	Page<Product> findByIsActive(boolean isActive, Pageable pageable);

	Page<Product> findAllByIsDeleteAndIsActiveAndCreateAtBetween(Boolean isDelete, Boolean isActive, Date dateStart,
			Date dateEnd, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isActive = true and p.isDelete=false "
			+ "AND (p.createAt BETWEEN :dateStart AND :dateEnd) "
			+ "AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% "
			+ "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
	Page<Product> selectAllMatchingAttributesByDateStartAndDateEnd(@Param("key") String key,
			@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isActive = true and p.isDelete=false " +
	// "AND (p.createAt BETWEEN :dateStart AND :dateEnd) " +
			"AND (:key iS NULL OR p.name LIKE %:key% OR p.introduce LIKE %:key% "
			+ "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
	Page<Product> selectAllMatchingAttributesByDateStartAndDateEnd(@Param("key") String key, Pageable pageable);

	// @Query("SELECT p FROM Product p WHERE p.id IN (SELECT fl.product.id FROM
	// FlashSaleDetail fl Where fl.id =?1)")
	// Page<Product> selectAllProductInFlashSale(Integer flashSaleId, Pageable
	// pageable);

	@Query("SELECT p FROM Product p WHERE p.id NOT IN :idProducts")
	Page<Product> findAllIdNotIn(@Param("idProducts") List<Integer> idProducts, Pageable pageable);

	Page<Product> findAllByIdNotAndCategory(Integer id, Category categories, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.account = ?1 AND p.category != ?2 AND p.isDelete = false AND p.isActive = true")
	Page<Product> findAllByAccountAndCategoryNot(Account account, Category category, Pageable pageable);

	Page<Product> findAllByNameContainingAndIsActiveTrueAndIsDeleteFalse(String name, Pageable pageable);

	Page<Product> findByCategoryInAndIdNotIn(List<Category> categories, List<Integer> idProduct, Pageable pageable);

}
