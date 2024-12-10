package com.toel.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toel.model.Account;
import com.toel.model.Address;
import com.toel.model.Bill;

import com.toel.model.OrderStatus;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

	@Query("SELECT b.id AS billId, \r\n" + "    a.id AS userId, \r\n" + "    b.totalPrice AS totalBillPrice, \r\n"
			+ "    b.priceShipping AS priceShippingBill, \r\n" + "    b.totalQuantity AS totalBillQuantity, \r\n"
			+ "    os.name AS orderStatus, \r\n" + "    b.createAt AS createdDatetime, \r\n"
			+ "    b.updateAt AS updatedDatetime, \r\n" + "    b.paymentMethod.name AS paymentMethod \r\n"
			+ "	 FROM Bill b JOIN \r\n" + "    b.account a \r\n" + "	 JOIN \r\n"
			+ "    b.orderStatus os  WHERE a.id = :userId  AND os.id = :orderStatus "
			+ "	 ORDER BY CASE WHEN :orderBy = 'create' THEN b.createAt "
			+ "              WHEN :orderBy = 'update' THEN b.updateAt " + "              ELSE b.createAt END DESC")
	List<Object[]> findBillsByUserIdAndOrderStatusOrderedByCreateOrUpdate(@Param("userId") Integer userId,
			@Param("orderStatus") Integer orderStatus, @Param("orderBy") String orderBy);

	@Query("SELECT \r\n" + //
			"    b.id AS billId, \r\n" + //
			"    a.id AS userId, \r\n" + //
			"    b.totalPrice AS totalBillPrice, \r\n" + //
			"    b.priceShipping AS priceShippingBill, \r\n" + //
			"    b.totalQuantity AS totalBillQuantity, \r\n" + //
			"    os.name AS orderStatus, \r\n" + //
			"    b.createAt AS createdDatetime, \r\n" + //
			"    b.updateAt AS updatedDatetime, \r\n" + "    b.paymentMethod.name AS paymentMethod \r\n" + //
			"FROM Bill b JOIN  b.account a \r\n" + //
			"JOIN  b.orderStatus os WHERE a.id = :userId \r\n" + //
			"ORDER BY b.createAt DESC\r\n" + //
			"")
	List<Object[]> findBillsByUserId(@Param("userId") Integer userId);

	// Seller (Update Shop)

	@Query("SELECT b FROM Bill b JOIN b.billDetails bd WHERE bd.product.account.id = ?1 "
			+ "AND (?2 IS NULL OR b.account.fullname LIKE CONCAT('%', ?2, '%')) GROUP BY b.id, b.account.fullname, b.createAt, b.totalQuantity, b.totalPrice, b.orderStatus.id ")
	Page<Bill> findAllByShopId(Integer shopId, String search, Pageable pageable);

	// Home Seller

	@Query("SELECT COUNT(bd.bill.id) FROM BillDetail bd  WHERE bd.bill.orderStatus.id = 1 AND bd.product.account.id = ?1")
	Integer getDonChoDuyet(Integer account_id);

	@Query("SELECT SUM(bd.quantity * bd.price) FROM BillDetail bd WHERE DATE(bd.bill.finishAt) = CURRENT_DATE AND bd.product.account.id = ?1 ")
	Double getDoanhSo(Integer account_id);

	@Query("SELECT  COALESCE(SUM((bd.price * bd.quantity) * (1 - (bd.bill.discountRate.discount / 100.0)) - (bd.price * bd.quantity * "
			+ "COALESCE(( SELECT COALESCE(vd.voucher.sale, 0)/ 100  FROM bd.bill.voucherDetails vd WHERE vd.bill = bd.bill and vd.voucher.typeVoucher.id=1),0))), 0) "
			+ " FROM BillDetail bd WHERE DATE(bd.bill.finishAt) = CURRENT_DATE AND bd.product.account.id = ?1 ")
	Double getDoanhThu(Integer account_id);

	@Query("SELECT MONTH(bd.bill.finishAt), SUM(bd.quantity * bd.price) " + "FROM BillDetail bd "
			+ "WHERE YEAR(bd.bill.finishAt) = ?1 AND bd.product.account.id = ?2 " + "GROUP BY MONTH(bd.bill.finishAt)"
			+ "ORDER BY MONTH(bd.bill.finishAt)")
	List<Object[]> getListDoanhSo(Integer year, Integer account_id);

	@Query("SELECT MONTH(bd.bill.finishAt), COALESCE(SUM((bd.price * bd.quantity) * (1 - (bd.bill.discountRate.discount / 100.0)) - (bd.price * bd.quantity * "
			+ "COALESCE(( SELECT COALESCE(vd.voucher.sale, 0)/ 100  FROM bd.bill.voucherDetails vd WHERE vd.bill = bd.bill and vd.voucher.typeVoucher.id=1),0))), 0) "
			+ "FROM BillDetail bd " + "WHERE YEAR(bd.bill.finishAt) = ?1 AND bd.product.account.id = ?2 "
			+ "GROUP BY MONTH(bd.bill.finishAt) " + "ORDER BY MONTH(bd.bill.finishAt)")
	List<Object[]> getListDoanhThu(Integer year, Integer account_id);

	// @Query("SELECT COALESCE(SUM(b.discountPrice),0) FROM BillDetail bd WHERE
	// bd.account.id =?1 AND ( ?2 IS NULL OR bd.bill.finishAt >= ?2) AND ( ?3 IS
	// NULL OR bd.bill.finishAt <= ?3 )")
	// Double calculateVoucherByShop_San(Integer account, Date dateStart, Date
	// dateEnd);

	@Query("SELECT DISTINCT YEAR(b.finishAt) FROM Bill b JOIN b.billDetails bd WHERE b.finishAt IS NOT NULL AND bd.product.account.id = ?1 ORDER BY YEAR(b.finishAt) DESC")
	List<Integer> getDistinctYears(Integer account_id);

	// Thong Ke Seller

	@Query("SELECT COALESCE(SUM((bd.quantity * bd.price)),0) " + "FROM BillDetail bd "
			+ "WHERE bd.product.account.id = :accountId AND bd.bill.orderStatus.id = 5 AND DATE(bd.bill.createAt)  BETWEEN COALESCE(:startDate, CURRENT_DATE) AND COALESCE(:endDate, CURRENT_DATE)")
	Double getTongDoanhSo(@Param("accountId") Integer accountId, @Param("startDate") Date dateStart,
			@Param("endDate") Date dateEnd);

	@Query("SELECT COALESCE(SUM((bd.price * bd.quantity) * (1 - (bd.bill.discountRate.discount / 100.0)) - (bd.price * bd.quantity * "
			+ "COALESCE(( SELECT COALESCE(vd.voucher.sale, 0)/ 100  FROM bd.bill.voucherDetails vd WHERE vd.bill = bd.bill and vd.voucher.typeVoucher.id=1),0))), 0) "
			+ "FROM BillDetail bd " + "WHERE bd.product.account.id = :accountId AND bd.bill.orderStatus.id = 5 "
			+ "AND DATE(bd.bill.createAt) BETWEEN COALESCE(:startDate, CURRENT_DATE) AND COALESCE(:endDate, CURRENT_DATE)")
	Double getTongDoanhThu(@Param("accountId") Integer accountId, @Param("startDate") Date dateStart,
			@Param("endDate") Date dateEnd);

	@Query("SELECT b FROM Bill b JOIN b.billDetails bd WHERE bd.product.account.id = :accountId "
			+ "AND DATE(bd.bill.createAt) BETWEEN COALESCE(:startDate, CURRENT_DATE) AND COALESCE(:endDate, CURRENT_DATE) GROUP BY b.id, b.account.fullname, b.createAt, b.totalQuantity, b.totalPrice, b.orderStatus.id")
	Page<Bill> getListThongKeBill(@Param("accountId") Integer accountId, @Param("startDate") Date dateStart,
			@Param("endDate") Date dateEnd, Pageable pageable);

	@Query("SELECT COUNT(b) FROM Bill b JOIN b.billDetails bd " + "WHERE bd.product.account.id = :accountId "
			+ "AND b.finishAt IS NOT NULL "
			+ "AND (:search IS NULL OR b.account.fullname LIKE CONCAT('%', :search, '%'))")
	Integer tongSoLuotMua(@Param("accountId") Integer accountId, @Param("search") String search);

	@Query("SELECT SUM(bd.quantity) FROM Bill b JOIN b.billDetails bd " + "WHERE bd.product.account.id = :accountId "
			+ "AND b.finishAt IS NOT NULL "
			+ "AND (:search IS NULL OR b.account.fullname LIKE CONCAT('%', :search, '%'))")
	Integer tongSoSP(@Param("accountId") Integer accountId, @Param("search") String search);

	@Query("SELECT COUNT(e) FROM Bill b JOIN b.billDetails bd JOIN bd.evalue e "
			+ "WHERE bd.product.account.id = :accountId "
			+ "AND (:search IS NULL OR b.account.fullname LIKE CONCAT('%', :search, '%'))")
	Integer tongSoLuotDanhGia(@Param("accountId") Integer accountId, @Param("search") String search);

	@Query("SELECT SUM(b.totalPrice) FROM Bill b JOIN b.billDetails bd " + "WHERE bd.product.account.id = :accountId "
			+ "AND b.finishAt IS NOT NULL "
			+ "AND (:search IS NULL OR b.account.fullname LIKE CONCAT('%', :search, '%'))")
	Double tongSotTien(@Param("accountId") Integer accountId, @Param("search") String search);

	@Query("SELECT " + "a.fullname AS khachHang, " + "SUM(bd.quantity) AS soSanPham, "
			+ "COUNT(DISTINCT b) AS luotMua, " + "COUNT(DISTINCT e) AS luotDanhGia, " + "SUM(b.totalPrice) AS soTien "
			+ "FROM Account a " + "INNER JOIN a.bills b " + "INNER JOIN b.billDetails bd " + "LEFT JOIN bd.evalue e "
			+ "WHERE bd.product.account.id = :accountId " + "AND b.finishAt IS NOT NULL "
			+ "AND (:search IS NULL OR a.fullname LIKE %:search%) " + "GROUP BY a.fullname, a.id")
	Page<Object[]> getListThongKeKhachHang(@Param("accountId") Integer accountId, @Param("search") String search,
			Pageable pageable);

	@Query("SELECT SUM(bd.quantity) " + "FROM Product p " + "JOIN p.billDetails bd "
			+ "WHERE p.account.id = :accountId " + "AND (:search IS NULL OR p.name LIKE %:search%)")
	Integer tongLuotBanSanPham(@Param("accountId") Integer account_id, @Param("search") String search);

	@Query("SELECT COUNT(e.id) " + "FROM Product p " + "JOIN p.billDetails bd " + "JOIN bd.evalue e "
			+ "WHERE p.account.id = :accountId " + "AND (:search IS NULL OR p.name LIKE %:search%)")
	Integer tongLuotDanhGia(@Param("accountId") Integer account_id, @Param("search") String search);

	@Query("SELECT COUNT(l.id) " + "FROM Product p " + "JOIN p.likes l " + "WHERE p.account.id = :accountId "
			+ "AND (:search IS NULL OR p.name LIKE %:search%)")
	Integer tongLuotYeuThich(@Param("accountId") Integer account_id, @Param("search") String search);

	@Query("SELECT AVG(e.star) " + "FROM Product p " + "JOIN p.billDetails bd " + "JOIN bd.evalue e "
			+ "WHERE p.account.id = :accountId " + "AND (:search IS NULL OR p.name LIKE %:search%)")
	Double tongTrungBinhLuotDanhGia(@Param("accountId") Integer account_id, @Param("search") String search);

	@Query("SELECT p.name AS nameSP, c.name AS theLoai, "
			+ "(SELECT SUM(bd.quantity) FROM BillDetail bd WHERE bd.product.id = p.id) AS luotBan, "
			+ "COUNT(DISTINCT e.id) AS luotDanhGia, " + "AVG(e.star) AS trungBinhDanhGia, "
			+ "COUNT(DISTINCT l.id) AS luotYeuThich " + "FROM Product p " + "JOIN p.category c "
			+ "LEFT JOIN p.billDetails bd " + "LEFT JOIN bd.evalue e " + "LEFT JOIN p.likes l "
			+ "WHERE p.account.id = :accountId " + "AND (:search IS NULL OR p.name LIKE %:search%) "
			+ "GROUP BY p.id, p.name")
	Page<Object[]> getListThongKeSanPham(@Param("accountId") Integer account_id, @Param("search") String search,
			Pageable pageable);

	@Query("Select b From Bill b Where b.orderStatus.id = :idOrderStatus AND b.finishAt BETWEEN  :dateStart AND :dateEnd ")
	Page<Bill> selectAllByFinishAtBetweenAndOrderStatus(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, @Param("idOrderStatus") Integer idOrderStatus, Pageable pageable);

	@Query("Select b From Bill b Where b.createAt BETWEEN  :dateStart AND :dateEnd")
	Page<Bill> selectAllByCreateAtBetweenAndOrderStatus(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, Pageable pageable);

	@Query("Select b From Bill b Where b.orderStatus.id = :idOrderStatus AND b.updateAt BETWEEN  :dateStart AND :dateEnd")
	Page<Bill> selectAllByUpdateAtBetweenAndOrderStatus(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, @Param("idOrderStatus") Integer idOrderStatus, Pageable pageable);

	@Query("Select b From Bill b Where b.orderStatus.id = :idOrderStatus AND b.createAt BETWEEN  :dateStart AND :dateEnd")
	Page<Bill> selectAllByCreateAtBetweenAndOrderStatus(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, @Param("idOrderStatus") Integer idOrderStatus, Pageable pageable);

	@Query(value = "SELECT * FROM Bills WHERE orderstatus_id = :orderstatusID", nativeQuery = true)
	List<Bill> findByOrderStatusId(@Param("orderstatusID") Integer orderstatusId);

	@Query("SELECT COALESCE(AVG(b.totalPrice),0) FROM Bill b WHERE b.account =?1")
	double calculateAGVTotalPriceByAccount(Account account);

	Integer countByAccount(Account account);

	@Query("SELECT a FROM Account a " + "WHERE a.id IN ( " + "    SELECT DISTINCT p.account.id " + "    FROM Product p "
			+ "    JOIN p.billDetails bd " + "    JOIN bd.bill b "
			+ "    WHERE b.finishAt BETWEEN :dateStart AND :dateEnd " + ") "
			+ "AND (:gender IS NULL OR a.gender = :gender) "
			+ "AND (LOWER(a.username) LIKE LOWER(CONCAT('%', :search, '%')) "
			+ "OR LOWER(a.fullname) LIKE LOWER(CONCAT('%', :search, '%')) "
			+ "OR LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%')) "
			+ "OR LOWER(a.phone) LIKE LOWER(CONCAT('%', :search, '%')))")
	List<Account> findByFinishAtBetweenAndGenderAndSearch(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, @Param("gender") Boolean gender, @Param("search") String search);

	List<Bill> findByOrderStatus(OrderStatus orderStatus);

	List<Bill> findByOrderStatusAndAccount(OrderStatus orderStatus, Account account);

	List<Bill> findAllByCreateAtBetweenAndOrderStatus(Date dateStart, Date dateEnd, OrderStatus orderStatus);

	List<Bill> findAllByUpdateAtBetweenAndOrderStatus(Date dateStart, Date dateEnd, OrderStatus orderStatus);

	// List<Bill> findAllByFinishAtBetweenAndOrderStatus(Date dateStart, Date
	// dateEnd, OrderStatus orderStatus);
	@Query("Select b From Bill b Where b.orderStatus.id = :idOrderStatus AND b.finishAt BETWEEN  :dateStart AND :dateEnd ")
	List<Bill> findAllByFinishAtBetweenAndOrderStatus(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd, @Param("idOrderStatus") Integer idOrderStatus);

	// thống kê khách hàng
	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN ?1 AND ?2")
	List<Account> selectAllByAccountAndFinishAt(Date dateStart, Date dateEnd);

	// thống kê khách hàng
	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN ?1 AND ?2 And b.account.gender = ?3")
	List<Account> selectAllByAccountAndGenderFinishAt(Date dateStart, Date dateEnd, Boolean gender);

	// thống kê khách hàng
	@Query("SELECT b.account FROM Bill b WHERE b.finishAt BETWEEN :finishDateStart AND :finishDateEnd "
			+ "AND (:gender IS NULL OR b.account.gender = :gender)"
			+ "AND (b.account.username LIKE %:username% OR b.account.fullname LIKE %:fullname% "
			+ "OR b.account.email LIKE %:email% OR b.account.phone LIKE %:phone%) ")
	List<Account> findAllByCreateAtBetweenAndGenderAndRoleAndUsernameContainingOrFullnameContainingOrEmailContainingOrPhoneContaining(
			@Param("finishDateStart") Date finishDateStart, @Param("finishDateEnd") Date finishDateEnd,
			@Param("gender") Boolean gender, @Param("username") String username, @Param("fullname") String fullname,
			@Param("email") String email, @Param("phone") String phone);

	List<Bill> findBillsByAccountId(Integer userId);

	@Query("SELECT b.account.id FROM Bill b WHERE b.id = ?1")
	Optional<Integer> findUserById(Integer billId);

	@Query("SELECT b.orderStatus.name  FROM Bill b JOIN  b.orderStatus os WHERE b.id = ?1")
	String findOrderStatusByBill(Integer billId);

	// Optional<Integer> findAddressByBill(Integer id);

	@Query("SELECT b.address FROM Bill b WHERE b.id = ?1")
	Optional<Address> findAddressByBill(Integer id);

	@Query("SELECT b FROM Bill b WHERE b.account.id = ?1 AND b.orderStatus.id = 5 AND b.orderStatus.id = 6")
	List<Bill> findAllByUser(Integer id_user);

	@Query("SELECT b FROM Bill b WHERE b.account.id = ?1 ORDER BY b.id DESC")
	List<Bill> findByAccountId(Integer account_id);

}
