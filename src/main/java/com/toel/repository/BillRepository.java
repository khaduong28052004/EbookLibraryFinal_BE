package com.toel.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toel.dto.seller.response.Response_DoanhSo;
import com.toel.dto.seller.response.Response_DoanhThu;

import com.toel.dto.seller.response.Response_Year;
import com.toel.model.Account;
// import com.toel.dto.user.response.Response_Bill;
import com.toel.model.Bill;
import com.toel.model.Role;

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

	@Query("SELECT b FROM Bill b JOIN b.billDetails bd WHERE bd.product.account.id = ?1 AND (?2 IS NULL OR b.account.fullname LIKE CONCAT('%', ?2, '%'))")
	Page<Bill> findAllByShopId(Integer shopId, String search, Pageable pageable);

	// Home Seller

	@Query("SELECT COUNT(b) FROM Bill b JOIN b.billDetails bd WHERE b.orderStatus.id = 1 AND bd.product.account.id = ?1")
	Integer getDonChoDuyet(Integer account_id);

	@Query("SELECT SUM(b.totalPrice) FROM Bill b JOIN b.billDetails bd WHERE b.finishAt = CURRENT_DATE AND bd.product.account.id = ?1 ")
	Double getDoanhSo(Integer account_id);

	@Query("SELECT SUM(b.totalPrice * (1 - (b.discountRate.discount / 100.0))) FROM Bill b JOIN b.billDetails bd WHERE b.finishAt = CURRENT_DATE AND bd.product.account.id = ?1 ")
	Double getDoanhThu(Integer account_id);

	@Query("SELECT b.totalPrice FROM Bill b JOIN b.billDetails bd WHERE YEAR(b.finishAt) = ?1 AND bd.product.account.id = ?2")
	List<Response_DoanhSo> getListDoanhSo(Integer year, Integer account_id);

	@Query("SELECT b.totalPrice * (1 - (b.discountRate.discount / 100.0)) FROM Bill b JOIN b.billDetails bd WHERE YEAR(b.finishAt) = ?1 AND bd.product.account.id = ?2")
	List<Response_DoanhThu> getListDoanhThu(Integer year, Integer account_id);

	@Query("SELECT COALESCE(AVG(b.discountPrice),0) FROM Bill b WHERE b.account.id =?1 AND ( ?2 IS NULL OR b.finishAt >= ?2) AND ( ?3 IS NULL OR b.finishAt <= ?3 )")
	Double calculateVoucherByShop_San(Integer account, Date dateStart, Date dateEnd);

	// @Query("SELECT b FROM Bill b WHERE b.account.id =?1 AND ( ?2 IS NULL OR
	// b.finishAt >= ?2) AND ( ?3 IS NULL OR b.finishAt <= ?3 )")
	// Page<Bill> selectBill(Integer account, LocalDate dateStart, LocalDate
	// dateEnd);
	@Query("SELECT DISTINCT YEAR(b.finishAt) FROM Bill b JOIN b.billDetails bd WHERE b.finishAt IS NOT NULL AND bd.product.account.id = ?1 ORDER BY YEAR(b.finishAt) DESC")
	List<Response_Year> getDistinctYears(Integer account_id);

	// Thong Ke Seller

	@Query("SELECT SUM(b.totalPrice) FROM Bill b JOIN b.billDetails bd WHERE bd.product.account.id =?1 AND b.finishAt BETWEEN  ?2 AND ?3")
	Double getTongDoanhSo(Integer account_id, Date dateStart, Date dateEnd);

	@Query("SELECT b.totalPrice * (1 - (b.discountRate.discount / 100.0)) FROM Bill b JOIN b.billDetails bd WHERE bd.product.account.id =?1 AND b.finishAt BETWEEN  ?2 AND ?3")
	Double getTongDoanhThu(Integer account_id, Date dateStart, Date dateEnd);

	@Query("SELECT b FROM Bill b JOIN b.billDetails bd WHERE bd.product.account.id =?1 AND b.finishAt BETWEEN  ?2 AND ?3")
	Page<Bill> getListThongKeBill(Integer account_id, Date dateStart, Date dateEnd, Pageable pageable);

	@Query("SELECT b.account.fullname, " +
			"SUM(bd.quantity), COUNT(b), COUNT(e), SUM(b.totalPrice) " +
			"FROM Bill b JOIN b.billDetails bd " +
			"JOIN b.account.evalues e " +
			"WHERE bd.product.account.id = ?1 " +
			"GROUP BY b.account.fullname")
	List<Object[]> getListThongKeKhachHang(Integer account_id);

	@Query("SELECT bd.product.name, " +
			"bd.product.category.name, " +
			"SUM(bd.quantity), " +
			"COUNT(DISTINCT e), " +
			"AVG(e.star), " +
			"COUNT(l.id) " + // Đếm số lượt like từ bảng Like
			"FROM Bill b " +
			"JOIN b.billDetails bd " +
			"LEFT JOIN bd.evalue e " +
			"LEFT JOIN bd.product.likes l " + // Tham gia bảng Like để đếm lượt thích
			"WHERE bd.product.account.id = ?1 " +
			"GROUP BY bd.product.name, bd.product.category.name")
	List<Object[]> getListThongKeSanPham(Integer account_id);

	@Query("Select b From Bill b Where b.orderStatus.id = :idOrderStatus AND (b.finishAt BETWEEN  :dateStart AND :dateEnd OR b.createAt BETWEEN  :dateStart AND :dateEnd)")
	Page<Bill> selectAllByCreateAtBetweenOrFinishAtBetweenAndOrderStatus(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd,
			@Param("idOrderStatus") Integer idOrderStatus, Pageable pageable);

	@Query("Select b From Bill b Where b.orderStatus.id = :idOrderStatus AND b.createAt BETWEEN  :dateStart AND :dateEnd")
	Page<Bill> selectAllByCreateAtBetweenAndOrderStatus(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd,
			@Param("idOrderStatus") Integer idOrderStatus, Pageable pageable);

	@Query(value = "SELECT * FROM Bills WHERE orderstatus_id = :orderstatusID", nativeQuery = true)
	List<Bill> findByOrderStatusId(@Param("orderstatusID") Integer orderstatusId);

	@Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Bill b WHERE b.account =?1")
	double calculateAGVTotalPriceByAccount(Account account);

	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN ?1 AND ?2")
	Page<Account> selectAllByAccountAndFinishAt(Date dateStart, Date dateEnd, Pageable pageable);

	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN ?1 AND ?2 And b.account.gender = ?3")
	Page<Account> selectAllByAccountAndGenderFinishAt(Date dateStart, Date dateEnd, Boolean gender, Pageable pageable);

	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN :finishDateStart AND :finishDateEnd " +
			"AND (:gender IS NULL OR b.account.gender = :gender)" +
			"AND (b.account.username LIKE %:username% OR b.account.fullname LIKE %:fullname% " +
			"OR b.account.email LIKE %:email% OR b.account.phone LIKE %:phone%) ")
	Page<Account> findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
			@Param("finishDateStart") Date finishDateStart,
			@Param("finishDateEnd") Date finishDateEnd,
			@Param("gender") Boolean gender,
			@Param("username") String username,
			@Param("fullname") String fullname,
			@Param("email") String email,
			@Param("phone") String phone,
			Pageable pageable);

	Integer countByAccount(Account account);

	@Query("SELECT bd.product.account FROM Bill b JOIN b.billDetails bd WHERE b.finishAt BETWEEN ?1 AND ?2")
	Page<Account> selectAllByShopAndFinishAt(Date dateStart, Date dateEnd, Pageable pageable);

	@Query("SELECT bd.product.account FROM Bill b JOIN b.billDetails bd WHERE b.finishAt BETWEEN ?1 AND ?2 And b.account.gender = ?3 ")
	Page<Account> selectAllByShopAndGenderFinishAt(Date dateStart, Date dateEnd, Boolean gender, Pageable pageable);

	@Query("SELECT bd.product.account FROM Bill b JOIN b.billDetails bd WHERE b.finishAt BETWEEN :finishDateStart AND :finishDateEnd " +
			"AND (:gender IS NULL OR b.account.gender = :gender)" +
			"AND (b.account.username LIKE %:username% OR b.account.fullname LIKE %:fullname% " +
			"OR b.account.email LIKE %:email% OR b.account.phone LIKE %:phone%) ")
	Page<Account> findAllShopByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
			@Param("finishDateStart") Date finishDateStart,
			@Param("finishDateEnd") Date finishDateEnd,
			@Param("gender") Boolean gender,
			@Param("username") String username,
			@Param("fullname") String fullname,
			@Param("email") String email,
			@Param("phone") String phone,
			Pageable pageable);

}
