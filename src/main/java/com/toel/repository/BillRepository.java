package com.toel.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toel.model.Account;
import com.toel.model.Bill;

import com.toel.model.OrderStatus;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
	@Query(value = "SELECT user.id as userID, bills.id as billID, "
			+ "bills.totalPrice as billTotalPrice,COALESCE(bills.discountPrice, 0), bills.priceShipping as billTotalShippingPrice, bills.totalQuantity as billTotalQuantity, "
			+ "bills.address_id as billAddressId, bills.orderstatus_id as billOrderStatusId, bills.createAt as createdDatetime, bills.updateAt as updatedDatetime, "
			+ "COALESCE(bills.discountrate_id, 0) as billDiscountRate, "
			+ "products.id as productId, products.name as productName, products.introduce as productIntroduce, "
			+ "billdetails.quantity as productQuantity, billdetails.price as productPrice, billdetails.discountPrice as productDiscountPrice, imageproducts.name as productImageURL, "
			+ "shop.id as shopId, shop.shopName, shop.avatar as shopAvatar, bills.paymentmethod_id\r\n "
			+ "FROM bills "
			+ "JOIN accounts user ON bills.account_id = user.id "
			+ "JOIN billdetails ON billdetails.bill_id = bills.id "
			+ "JOIN products ON billdetails.product_id = products.id "
			+ "JOIN accounts shop ON shop.id = products.account_id "
			+ "LEFT JOIN imageproducts ON imageproducts.product_id = products.id "
			+ "LEFT JOIN discountrates ON discountrates.id = bills.discountrate_id "
			+ "WHERE user.id = :userId AND bills.orderstatus_id= :idOrderStatus "
			+ "ORDER BY bills.createAt DESC", nativeQuery = true)
	List<Object[]> getBillsByUserIdNOrderStatusOrderByCreateAt(@Param("userId") Integer userId,
			@Param("idOrderStatus") Integer idOrderStatus);

	@Query(value = "SELECT user.id as userID, bills.id as billID, "
			+ "bills.totalPrice as billTotalPrice, COALESCE(bills.discountPrice, 0), bills.priceShipping as billTotalShippingPrice, bills.totalQuantity as billTotalQuantity, "
			+ "bills.address_id as billAddressId, bills.orderstatus_id as billOrderStatusId, bills.createAt as createdDatetime, bills.updateAt as updatedDatetime, "
			+ "COALESCE(bills.discountrate_id, 0) as billDiscountRate, "
			+ "products.id as productId, products.name as productName, products.introduce as productIntroduce, "
			+ "billdetails.quantity as productQuantity, billdetails.price as productPrice, billdetails.discountPrice as productDiscountPrice, imageproducts.name as productImageURL, "
			+ "shop.id as shopId, shop.shopName, shop.avatar as shopAvatar,  bills.paymentmethod_id \r\n "
			+ "FROM bills "
			+ "JOIN accounts user ON bills.account_id = user.id "
			+ "JOIN billdetails ON billdetails.bill_id = bills.id "
			+ "JOIN products ON billdetails.product_id = products.id "
			+ "JOIN accounts shop ON shop.id = products.account_id "
			+ "LEFT JOIN imageproducts ON imageproducts.product_id = products.id "
			+ "LEFT JOIN discountrates ON discountrates.id = bills.discountrate_id "
			+ "WHERE user.id = :userId AND bills.orderstatus_id= :idOrderStatus "
			+ "ORDER BY bills.updateAt DESC", nativeQuery = true)
	List<Object[]> getBillsByUserIdNOrderStatusOrderByUpdateAt(@Param("userId") Integer userId,
			@Param("idOrderStatus") Integer idOrderStatus);

	@Query(value = "SELECT user.id as userID, bills.id as billID,\r\n"
			+ "bills.totalPrice as billTotalPrice,COALESCE(bills.discountPrice, 0),   bills.priceShipping as billTotalShippingPrice, bills.totalQuantity as billTotalQuantity,\r\n"
			+ "bills.address_id as billAddressId, bills.orderstatus_id as billOrderStatusId,  bills.createAt as createdDatetime, bills.updateAt as updatedDatetime,\r\n"
			+ "COALESCE(bills.discountrate_id, 0) as billDiscountRate,\r\n"
			+ "products.id as productId, products.name as productName,products.introduce as productIntroduce, \r\n"
			+ "billdetails.quantity as productQuantity , billdetails.price as productPrice, billdetails.discountPrice as productDiscountPrice, imageproducts.name as productImageURL,\r\n"

			+ "shop.id as shopId,  shop.shopName, shop.avatar as  shopAvatar,  bills.paymentmethod_id\r\n"

			+ "FROM\r\n"
			+ "bills JOIN accounts user ON bills.account_id = user.id \r\n"
			+ "JOIN billdetails ON billdetails.bill_id = bills.id\r\n"
			+ "JOIN products ON billdetails.product_id = products.id\r\n"
			+ "JOIN accounts shop ON shop.id = products.account_id\r\n"
			+ "LEFT JOIN imageproducts ON imageproducts.product_id = products.id\r\n"
			+ "LEFT JOIN discountrates ON discountrates.id =  bills.discountrate_id\r\n"
			+ "WHERE user.id = :userId \r\n"
			+ "ORDER BY  createdDatetime DESC ", nativeQuery = true)
	List<Object[]> getBillsByUserIdAll(@Param("userId") Integer userId);

	// Seller (Update Shop)

	@Query("SELECT b FROM Bill b JOIN b.billDetails bd WHERE bd.product.account.id = ?1 " +
			"AND (?2 IS NULL OR b.account.fullname LIKE CONCAT('%', ?2, '%')) GROUP BY b.id, b.account.fullname, b.createAt, b.totalQuantity, b.totalPrice, b.orderStatus.id ")
	Page<Bill> findAllByShopId(Integer shopId, String search, Pageable pageable);

	// Home Seller

	@Query("SELECT COUNT(b) FROM Bill b JOIN b.billDetails bd WHERE b.orderStatus.id = 1 AND bd.product.account.id = ?1")
	Integer getDonChoDuyet(Integer account_id);

	@Query("SELECT SUM(b.totalPrice) FROM Bill b JOIN b.billDetails bd WHERE b.finishAt = CURRENT_DATE AND bd.product.account.id = ?1 ")
	Double getDoanhSo(Integer account_id);

	@Query("SELECT SUM(b.totalPrice * (1 - (b.discountRate.discount / 100.0))) FROM Bill b JOIN b.billDetails bd WHERE b.finishAt = CURRENT_DATE AND bd.product.account.id = ?1 ")
	Double getDoanhThu(Integer account_id);

	@Query("SELECT MONTH(b.finishAt), SUM(b.totalPrice) " +
			"FROM Bill b JOIN b.billDetails bd " +
			"WHERE YEAR(b.finishAt) = ?1 AND bd.product.account.id = ?2 " +
			"GROUP BY MONTH(b.finishAt) " +
			"ORDER BY MONTH(b.finishAt)")
	List<Object[]> getListDoanhSo(Integer year, Integer account_id);

	@Query("SELECT MONTH(b.finishAt), SUM(b.totalPrice * (1 - (b.discountRate.discount / 100.0))) " +
			"FROM Bill b JOIN b.billDetails bd " +
			"WHERE YEAR(b.finishAt) = ?1 AND bd.product.account.id = ?2 " +
			"GROUP BY MONTH(b.finishAt) " +
			"ORDER BY MONTH(b.finishAt)")
	List<Object[]> getListDoanhThu(Integer year, Integer account_id);

	@Query("SELECT COALESCE(AVG(b.discountPrice),0) FROM Bill b WHERE b.account.id =?1 AND ( ?2 IS NULL OR b.finishAt >= ?2) AND ( ?3 IS NULL OR b.finishAt <= ?3 )")
	Double calculateVoucherByShop_San(Integer account, Date dateStart, Date dateEnd);

	// @Query("SELECT b FROM Bill b WHERE b.account.id =?1 AND ( ?2 IS NULL OR
	// b.finishAt >= ?2) AND ( ?3 IS NULL OR b.finishAt <= ?3 )")
	// Page<Bill> selectBill(Integer account, LocalDate dateStart, LocalDate
	// dateEnd);
	@Query("SELECT DISTINCT YEAR(b.finishAt) FROM Bill b JOIN b.billDetails bd WHERE b.finishAt IS NOT NULL AND bd.product.account.id = ?1 ORDER BY YEAR(b.finishAt) DESC")
	List<Integer> getDistinctYears(Integer account_id);

	// Thong Ke Seller

	@Query("SELECT SUM(b.totalPrice) FROM Bill b JOIN b.billDetails bd " +
			"WHERE bd.product.account.id = :accountId AND b.orderStatus.id = 5 AND b.createAt BETWEEN COALESCE(:startDate, CURRENT_DATE) AND COALESCE(:endDate, CURRENT_DATE)")
	Double getTongDoanhSo(
			@Param("accountId") Integer accountId,
			@Param("startDate") Date dateStart,
			@Param("endDate") Date dateEnd);

	@Query("SELECT SUM(b.totalPrice * (1 - (b.discountRate.discount / 100.0))) FROM Bill b JOIN b.billDetails bd " +
			"WHERE bd.product.account.id = :accountId AND b.orderStatus.id = 5 AND b.createAt BETWEEN COALESCE(:startDate, CURRENT_DATE) AND COALESCE(:endDate, CURRENT_DATE)")
	Double getTongDoanhThu(
			@Param("accountId") Integer accountId,
			@Param("startDate") Date dateStart,
			@Param("endDate") Date dateEnd);

	@Query("SELECT b FROM Bill b JOIN b.billDetails bd WHERE bd.product.account.id = :accountId " +
			"AND b.createAt BETWEEN COALESCE(:startDate, CURRENT_DATE) AND COALESCE(:endDate, CURRENT_DATE) GROUP BY b.id, b.account.fullname, b.createAt, b.totalQuantity, b.totalPrice, b.orderStatus.id")
	Page<Bill> getListThongKeBill(
			@Param("accountId") Integer accountId,
			@Param("startDate") Date dateStart,
			@Param("endDate") Date dateEnd,
			Pageable pageable);

	@Query("SELECT COUNT(b) FROM Bill b JOIN b.billDetails bd " +
			"WHERE bd.product.account.id = :accountId " +
			"AND b.finishAt IS NOT NULL " +
			"AND (:search IS NULL OR b.account.fullname LIKE CONCAT('%', :search, '%'))")
	Integer tongSoLuotMua(@Param("accountId") Integer accountId, @Param("search") String search);

	@Query("SELECT SUM(bd.quantity) FROM Bill b JOIN b.billDetails bd " +
			"WHERE bd.product.account.id = :accountId " +
			"AND b.finishAt IS NOT NULL " +
			"AND (:search IS NULL OR b.account.fullname LIKE CONCAT('%', :search, '%'))")
	Integer tongSoSP(@Param("accountId") Integer accountId, @Param("search") String search);

	@Query("SELECT COUNT(e) FROM Bill b JOIN b.billDetails bd JOIN bd.evalue e " +
			"WHERE bd.product.account.id = :accountId " +
			"AND (:search IS NULL OR b.account.fullname LIKE CONCAT('%', :search, '%'))")
	Integer tongSoLuotDanhGia(@Param("accountId") Integer accountId, @Param("search") String search);

	@Query("SELECT SUM(b.totalPrice) FROM Bill b JOIN b.billDetails bd " +
			"WHERE bd.product.account.id = :accountId " +
			"AND b.finishAt IS NOT NULL " +
			"AND (:search IS NULL OR b.account.fullname LIKE CONCAT('%', :search, '%'))")
	Double tongSotTien(@Param("accountId") Integer accountId, @Param("search") String search);

	@Query("SELECT b.account.fullname AS khachHang, " +
			"SUM(bd.quantity) AS soSanPham , COUNT(b) AS luotMua, COUNT(e) AS luotDanhGia, SUM(b.totalPrice) AS soTien "
			+
			"FROM Bill b JOIN b.billDetails bd " +
			"JOIN bd.evalue e " +
			"WHERE bd.product.account.id = :accountId  " +
			"AND b.finishAt IS NOT NULL " +
			"AND (:search IS NULL OR b.account.fullname LIKE %:search%) " +
			"GROUP BY b.account.fullname")
	Page<Object[]> getListThongKeKhachHang(
			@Param("accountId") Integer accountId,
			@Param("search") String search,
			Pageable pageable);

	@Query("SELECT SUM(bd.quantity) FROM Bill b JOIN b.billDetails bd JOIN bd.product p WHERE bd.product.account.id = :accountId AND (:search IS NULL OR p.name LIKE %:search%)")
	Integer tongLuotBanSanPham(@Param("accountId") Integer account_id, @Param("search") String search);

	@Query("SELECT COUNT(bd.evalue.id) FROM Bill b JOIN b.billDetails bd JOIN bd.product p WHERE bd.product.account.id = :accountId AND (:search IS NULL OR p.name LIKE %:search%)")
	Integer tongLuotDanhGia(@Param("accountId") Integer account_id, @Param("search") String search);

	@Query("SELECT SUM(SIZE(bd.product.likes)) FROM Bill b JOIN b.billDetails bd JOIN bd.product p WHERE bd.product.account.id = :accountId AND (:search IS NULL OR p.name LIKE %:search%)")
	Integer tongLuotYeuThich(@Param("accountId") Integer account_id, @Param("search") String search);

	@Query("SELECT AVG(bd.evalue.star) FROM Bill b JOIN b.billDetails bd JOIN bd.product p WHERE bd.product.account.id = :accountId AND (:search IS NULL OR p.name LIKE %:search%) ")
	Double tongTrungBinhLuotDanhGia(@Param("accountId") Integer account_id, @Param("search") String search);

	@Query("SELECT p.name AS nameSP , c.name AS theLoai, SUM(bd.quantity) AS luotBan, COUNT(e.id) AS luotDanhGia, AVG(e.star) AS trungBinhDanhGia, COUNT(l.id) AS luotYeuThich "
			+
			"FROM Bill b JOIN b.billDetails bd " +
			"LEFT JOIN bd.evalue e LEFT JOIN bd.product.likes l " +
			"JOIN bd.product p JOIN p.category c " +
			"WHERE p.account.id = :accountId " +
			"AND (:search IS NULL OR p.name LIKE %:search%) " +
			"GROUP BY p.name, c.name")
	Page<Object[]> getListThongKeSanPham(@Param("accountId") Integer account_id, @Param("search") String search,
			Pageable pageable);

	@Query("Select b From Bill b Where b.orderStatus.id = :idOrderStatus AND b.finishAt BETWEEN  :dateStart AND :dateEnd ")
	Page<Bill> selectAllByFinishAtBetweenAndOrderStatus(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd,
			@Param("idOrderStatus") Integer idOrderStatus, Pageable pageable);

	@Query("Select b From Bill b Where b.createAt BETWEEN  :dateStart AND :dateEnd")
	Page<Bill> selectAllByCreateAtBetweenAndOrderStatus(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, Pageable pageable);

	@Query("Select b From Bill b Where b.orderStatus.id = :idOrderStatus AND b.updateAt BETWEEN  :dateStart AND :dateEnd")
	Page<Bill> selectAllByUpdateAtBetweenAndOrderStatus(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd,
			@Param("idOrderStatus") Integer idOrderStatus, Pageable pageable);

	@Query(value = "SELECT * FROM Bills WHERE orderstatus_id = :orderstatusID", nativeQuery = true)
	List<Bill> findByOrderStatusId(@Param("orderstatusID") Integer orderstatusId);

	@Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Bill b WHERE b.account =?1")
	double calculateAGVTotalPriceByAccount(Account account);

	Integer countByAccount(Account account);

	@Query("SELECT a FROM Account a " +
			"WHERE a.id IN ( " +
			"    SELECT DISTINCT p.account.id " +
			"    FROM Product p " +
			"    JOIN p.billDetails bd " +
			"    JOIN bd.bill b " +
			"    WHERE b.finishAt BETWEEN :dateStart AND :dateEnd " +
			") " +
			"AND (:gender IS NULL OR a.gender = :gender) " +
			"AND (LOWER(a.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
			"OR LOWER(a.fullname) LIKE LOWER(CONCAT('%', :search, '%')) " +
			"OR LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
			"OR LOWER(a.phone) LIKE LOWER(CONCAT('%', :search, '%')))")
	List<Account> findByFinishAtBetweenAndGenderAndSearch(
			@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd,
			@Param("gender") Boolean gender,
			@Param("search") String search);

	List<Bill> findByOrderStatus(OrderStatus orderStatus);

	List<Bill> findByOrderStatusAndAccount(OrderStatus orderStatus, Account account);

	List<Bill> findAllByCreateAtBetweenAndOrderStatus(Date dateStart, Date dateEnd, OrderStatus orderStatus);

	List<Bill> findAllByFinishAtBetweenAndOrderStatus(Date dateStart, Date dateEnd, OrderStatus orderStatus);

	// thống kê khách hàng
	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN ?1 AND ?2")
	List<Account> selectAllByAccountAndFinishAt(Date dateStart, Date dateEnd);

	// thống kê khách hàng
	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN ?1 AND ?2 And b.account.gender = ?3")
	List<Account> selectAllByAccountAndGenderFinishAt(Date dateStart, Date dateEnd, Boolean gender);

	// thống kê khách hàng
	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN :finishDateStart AND :finishDateEnd " +
			"AND (:gender IS NULL OR b.account.gender = :gender)" +
			"AND (b.account.username LIKE %:username% OR b.account.fullname LIKE %:fullname% " +
			"OR b.account.email LIKE %:email% OR b.account.phone LIKE %:phone%) ")
	List<Account> findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
			@Param("finishDateStart") Date finishDateStart,
			@Param("finishDateEnd") Date finishDateEnd,
			@Param("gender") Boolean gender,
			@Param("username") String username,
			@Param("fullname") String fullname,
			@Param("email") String email,
			@Param("phone") String phone);
}
