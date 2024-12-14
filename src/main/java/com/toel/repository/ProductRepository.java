package com.toel.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.Account;
import com.toel.model.Bill;
import com.toel.model.BillDetail;
import com.toel.model.Category;
import com.toel.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	Page<Product> findAllByIdNotAndCategory(Integer id, Category categories, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.account = ?1 AND p.id != ?2 AND p.isDelete = false AND p.isActive = true")
	Page<Product> findAllByAccountAndIdNot(Account account, Integer id, Pageable pageable);

	Page<Product> findAllByNameContainingAndIsActiveTrueAndIsDeleteFalse(String name, Pageable pageable);

	Page<Product> findByCategoryInAndIdIn(List<Category> categories, List<Integer> idProduct, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE ((p.price- ((p.price*p.sale)/100)) between ?1 AND ?2) AND (id IN ?3)")
	Page<Product> findByPriceBetweenAndIdIn(double priceMin, double priceMax, List<Integer> idProduct,
			Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isActive = false and p.isDelete=false "
			+ "AND (:price IS NULL OR p.price = :price OR p.sale = :price )"
			+ "AND (:key iS NULL OR p.name LIKE %:key%  "
			+ "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
	Page<Product> selectAllByActiveAndDeleteAndMatchingAttributes(@Param("key") String key,
			@Param("price") Double price, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isActive = true "
			+ "AND (:price IS NULL OR p.price = :price OR p.sale = :price )"
			+ "AND (:key iS NULL OR p.name LIKE %:key%  "
			+ "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
	Page<Product> selectAllByActiveAndMatchingKey(@Param("key") String key, @Param("price") Double price,
			Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isActive != false And p.isDelete != true "
			+ "AND (:price IS NULL OR p.price = :price OR p.sale = :price )"
			+ "AND (:key iS NULL OR p.name LIKE %:key%  "
			+ "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
	Page<Product> selectAllMatchingKey(@Param("key") String key, @Param("price") Double price, Pageable pageable);

	@Query("SELECT p FROM Product p where p.account.id = ?1 AND p.isDelete = false "
			+ "AND (?2 IS NULL OR p.name LIKE CONCAT('%', ?2, '%'))")
	Page<Product> findByAccountId(Integer account_id, String search, Pageable pageable);

	@Query("SELECT p FROM Product p where p.account.id = ?1 AND p.isDelete = false ")
	List<Product> findByAccountId(Integer account_id);

	@Query("SELECT p FROM Product p WHERE p.isDelete=false and p.isActive=true AND (:search IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(:search), '%')) and p.id NOT IN (SELECT fl.product.id FROM FlashSaleDetail fl Where fl.flashSale.id = :flashSaleId)")
	Page<Product> selectAllProductNotInFlashSale(@Param("flashSaleId") Integer flashSaleId,
			@Param("search") String search, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isDelete=false and p.isActive=true and p.id IN (SELECT fl.product.id FROM FlashSaleDetail fl Where fl.flashSale.id =?1)")
	Page<Product> selectAllProductInFlashSale(Integer flashSaleId, Pageable pageable);

	Page<Product> findAllByIsDeleteAndIsActive(Boolean isDelete, Boolean isActive, Pageable pageable);

	List<Product> findAllByAccount(Account account);

	@Query(value = "SELECT * FROM products WHERE id not IN( SELECT id FROM products WHERE isActive = ?1 and isDelete = ?2)", nativeQuery = true)
	Page<Product> findAllByIsActiveNotAndIsDeleteNot(Boolean isActive, Boolean isDelete, Pageable pageable);

	Page<Product> findByIsActive(boolean isActive, Pageable pageable);

	List<Product> findAllByIsDeleteAndIsActiveAndCreateAtBetween(Boolean isDelete, Boolean isActive, Date dateStart,
			Date dateEnd);

	@Query("SELECT p FROM Product p WHERE p.isActive = true and p.isDelete=false "
			+ "AND (p.createAt BETWEEN :dateStart AND :dateEnd) " + "AND (:key iS NULL OR p.name LIKE %:key%  "
			+ "OR p.writerName LIKE %:key% OR p.publishingCompany LIKE %:key%)")
	List<Product> selectAllMatchingAttributesByDateStartAndDateEnd(@Param("key") String key,
			@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	// thống kê seller
	@Query("SELECT b.account FROM Product b WHERE b.createAt BETWEEN ?1 AND ?2")
	List<Account> selectAllByProductAndCreateAt(Date dateStart, Date dateEnd);

	// thống kê seller
	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN ?1 AND ?2 And b.account.gender = ?3")
	List<Account> selectAllByProductAndGenderFinishAt(Date dateStart, Date dateEnd, Boolean gender);

	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN :finishDateStart AND :finishDateEnd "
			+ "AND (:gender IS NULL OR b.account.gender = :gender)"
			+ "AND (b.account.username LIKE %:username% OR b.account.fullname LIKE %:fullname% "
			+ "OR b.account.email LIKE %:email% OR b.account.phone LIKE %:phone%) ")
	List<Account> findAllByProductCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
			@Param("finishDateStart") Date finishDateStart, @Param("finishDateEnd") Date finishDateEnd,
			@Param("gender") Boolean gender, @Param("username") String username, @Param("fullname") String fullname,
			@Param("email") String email, @Param("phone") String phone);

	@Query("SELECT p FROM Product p WHERE p.id NOT IN :idProducts AND p.account.id != :idShop AND p.isActive = true AND p.isDelete=false AND p.account.status = true")
	Page<Product> findAllIdNotIn(@Param("idProducts") List<Integer> idProducts, @Param("idShop") Integer idShop,
			Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.account.id = :idShop AND p.isActive = true AND p.isDelete=false AND p.account.status = true")
	List<Product> findAllIdIn(@Param("idShop") Integer idShop);

	@Query("SELECT p FROM Product p WHERE p.isActive = true AND p.isDelete = false AND p.account.status = true")
	List<Product> findAllProduct(Sort sort);

	@Query(value = "SELECT * FROM products p WHERE p.isDelete=false and p.isActive=false and p.createAt < NOW() - INTERVAL 7 DAY", nativeQuery = true)
	List<Product> findAllCreatedBeforeSevenDays();

	@Query("SELECT COUNT(f) FROM Product f WHERE f.account.id = :accountId")
	Integer countProductByAccountId(@Param("accountId") Integer accountId);

	@Query("SELECT DISTINCT p FROM Product p " + "LEFT JOIN BillDetail bd ON bd.product = p " + "WHERE p.id IN :ids "
			+ "AND p.isActive = true " + "AND p.isDelete = false " + "GROUP BY p.id "
			+ "ORDER BY COALESCE(SUM(bd.quantity), 0) DESC")
	Page<Product> findProductsByIdsSortedByTotalSales(@Param("ids") List<Integer> ids, Pageable pageable);

	List<Product> findByBillDetails(List<BillDetail> billDetails);

	@Query("SELECT p FROM Product p " + "WHERE p.isActive = true " + "AND p.isDelete = false " + "  AND p.quantity > 0"
			+ "AND p.account.status = true " + "AND p.id IN (" + "    SELECT bd.product.id " + "    FROM BillDetail bd "
			+ "    WHERE bd.bill.account.id = ?1 " + "    AND bd.bill.orderStatus.id IN (4, 5) "
			+ "    GROUP BY bd.product.id " + ")")
	List<Product> findAllByBillOfUser(Integer id_user);

	@Query("SELECT bd.product.id " + "FROM BillDetail bd " + "WHERE bd.bill.orderStatus.id IN (4, 5) "
			+ "GROUP BY bd.product.id")
	List<Integer> selectIdBillDetailTopProduct(Pageable pageable);

	@Query("SELECT bd.product.id " + "FROM BillDetail bd " + "WHERE bd.bill.orderStatus.id IN (4, 5) "
			+ "AND bd.product.id NOT IN :excludedIds " + "GROUP BY bd.product.id")
	List<Integer> selectIdBillDetailTopProductNotIds(@Param("excludedIds") List<Integer> excludedIds,
			Pageable pageable);

	@Query("SELECT p FROM Product p " + "WHERE p.isActive = true " + "AND p.isDelete = false " + "  AND p.quantity > 0"
			+ "AND p.account.status = true " + "AND p.id IN ?1")
	List<Product> selectProductInIdProduct(List<Integer> ids);

	@Query("SELECT p " + "FROM Product p " + "JOIN BillDetail bd ON p.id = bd.product.id "
			+ "JOIN Bill b ON bd.bill.id = b.id " + "WHERE b IN :bills " + "GROUP BY p.id "
			+ "ORDER BY SUM(bd.quantity) DESC")
	List<Product> findTop10ByBillDetails(@Param("bills") List<Bill> bills);

	@Query("SELECT p FROM Product p " + "LEFT JOIN BillDetail bd ON p.id = bd.product.id "
			+ "LEFT JOIN Evalue e ON e.product.id = p.id " + "LEFT JOIN Like l ON l.product.id = p.id "
			+ "WHERE p.isDelete = false AND p.isActive = true " + "GROUP BY p.id " + "ORDER BY CASE "
			+ "           WHEN :keySearch = 'moi' THEN p.id "
			+ "           WHEN :keySearch = 'danh gia' THEN COUNT(e.id) "
			+ "           WHEN :keySearch = 'luot ban' THEN SUM(bd.quantity) "
			+ "           WHEN :keySearch = 'yeu thich' THEN COUNT(l.id) " + "         END DESC")
	List<Product> findChatBotDESC(@Param("keySearch") String keySearch);

	@Query("SELECT p FROM Product p " + "LEFT JOIN BillDetail bd ON p.id = bd.product.id "
			+ "LEFT JOIN Evalue e ON e.product.id = p.id " + "LEFT JOIN Like l ON l.product.id = p.id "
			+ "WHERE p.isDelete = false AND p.isActive = true " + "GROUP BY p.id " + "ORDER BY CASE "
			+ "           WHEN :keySearch = 'moi' THEN p.id "
			+ "           WHEN :keySearch = 'danh gia' THEN COUNT(e.id) "
			+ "           WHEN :keySearch = 'luot ban' THEN SUM(bd.quantity) "
			+ "           WHEN :keySearch = 'yeu thich' THEN COUNT(l.id) " + "         END DESC")
	List<Product> findChatBotASC(@Param("keySearch") String keySearch);

	@Query("SELECT p FROM Product p " + "LEFT JOIN BillDetail bd ON p.id = bd.product.id "
			+ "LEFT JOIN Evalue e ON e.product.id = p.id " + "LEFT JOIN Like l ON l.product.id = p.id "
			+ "WHERE p.isDelete = false AND p.isActive = true " + "AND " + "  CASE "
			+ "    WHEN :keySearch = 'moi' THEN p.createAt BETWEEN :dateStart AND :dateEnd "
			+ "    WHEN :keySearch = 'danh gia' THEN e.createAt BETWEEN :dateStart AND :dateEnd "
			+ "    WHEN :keySearch = 'luot ban' THEN bd.bill.createAt BETWEEN :dateStart AND :dateEnd"
			+ "    WHEN :keySearch = 'yeu thich' THEN l.createAt BETWEEN :dateStart AND :dateEnd" + "  END "
			+ "GROUP BY p.id " + "ORDER BY " + "  CASE " + "    WHEN :keySearch = 'moi' THEN p.id "
			+ "    WHEN :keySearch = 'danh gia' THEN COUNT(e.id) "
			+ "    WHEN :keySearch = 'luot ban' THEN SUM(bd.quantity) "
			+ "    WHEN :keySearch = 'yeu thich' THEN COUNT(l.id) " + "  END DESC")
	List<Product> findChatBotByDateDESC(@Param("keySearch") String keySearch, @Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);

	@Query("SELECT p FROM Product p " + "LEFT JOIN BillDetail bd ON p.id = bd.product.id "
			+ "LEFT JOIN Evalue e ON e.product.id = p.id " + "LEFT JOIN Like l ON l.product.id = p.id "
			+ "WHERE p.isDelete = false AND p.isActive = true " + "AND " + "  CASE "
			+ "    WHEN :keySearch = 'moi' THEN p.createAt BETWEEN :dateStart AND :dateEnd "
			+ "    WHEN :keySearch = 'danh gia' THEN e.createAt BETWEEN :dateStart AND :dateEnd "
			+ "    WHEN :keySearch = 'luot ban' THEN bd.bill.createAt BETWEEN :dateStart AND :dateEnd"
			+ "    WHEN :keySearch = 'yeu thich' THEN l.createAt BETWEEN :dateStart AND :dateEnd" + "  END "
			+ "GROUP BY p.id " + "ORDER BY " + "  CASE " + "    WHEN :keySearch = 'moi' THEN p.id "
			+ "    WHEN :keySearch = 'danh gia' THEN COUNT(e.id) "
			+ "    WHEN :keySearch = 'luot ban' THEN SUM(bd.quantity) "
			+ "    WHEN :keySearch = 'yeu thich' THEN COUNT(l.id) " + "  END ASC")
	List<Product> findChatBotByDateASC(@Param("keySearch") String keySearch, @Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);
	
	// @Query("SELECT p.id, p.name, sum(billdetails.quantity) as totalSell\r\n" + //
	// "from BillDetail bd\r\n" + //
	// "join Product p on bd.product.id = p.id \r\n" + //
	// "join Bill on bd.bill.id = b.id\r\n" + //
	// "where p.isActive = 1 and p.account.id = ?1\r\n" + //
	// "and bills.createAt >= DATE_SUB(NOW())\r\n" + //
	// "GROUP BY p.id, p.name\r\n")
	// List<Product> findTopProductShop(Integer id_Shop);

	List<Product> findByCategory(Category category);

	@Query("SELECT p FROM Product p WHERE p.category IN :categories")
	List<Product> findByCategoryIn(@Param("categories") List<Category> categories);
	
	

}
