package com.toel.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Account;
import com.toel.model.Like;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.toel.model.Product;

import io.lettuce.core.dynamic.annotation.Param;

public interface LikeRepository extends JpaRepository<Like, Integer> {
       @Query("SELECT l.product.id AS productId, COUNT(l.id) AS likeCount " +
                     "FROM Like l " +
                     "GROUP BY l.product.id " +
                     "ORDER BY likeCount DESC")
       List<Map<String, Object>> findTopLikedProducts();

	Like findByAccountAndProduct(Account account, Product product);

	List<Like> findAllByAccount(Account account);

	@Query("Select COUNT(l) FROM Like l WHERE l.product.account.id = ?1")
	Integer getLike(Integer account_id);

	List<Like> findAllByProduct(Product product);

	@Query("Select COUNT(l) FROM Like l WHERE l.product = :product and l.createAt BETWEEN :dateStart AND :dateEnd")
	Integer countByProductAndCreateAt(@Param("product") Product product, @Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);

	@Query("SELECT p From Product p WHERE p.id IN (SELECT DISTINCT l.product.id FROM Like l Where l.createAt BETWEEN :dateStart AND :dateEnd)")
	List<Product> selectAllProduct(@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	@Query("SELECT p From Product p WHERE p.id IN (SELECT DISTINCT l.product.id FROM Like l Where l.createAt BETWEEN :dateStart AND :dateEnd AND (:key iS NULL OR l.product.name LIKE %:key% OR l.product.writerName LIKE %:key% OR l.product.publishingCompany LIKE %:key%))")
	List<Product> selectAllProductByDateStartDateEnd(@Param("key") String key, @Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);

	@Query("SELECT lk.product.id FROM  Like lk WHERE lk.product.isActive = true AND lk.product.isDelete = false AND lk.product.account.status = true  Group by lk.product.id Order By COUNT(lk.product.id) DESC ")
	List<Integer> selectIdLikeByTopProductLike(Pageable pageable);

	@Query("SELECT lk FROM Like lk WHERE lk.product.id IN ?1")
	List<Like> selectAllByListID(List<Integer> listIdLikes);

}
