package com.toel.repository;

import java.lang.foreign.Linker.Option;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.BillDetail;
import com.toel.model.FlashSaleDetail;
import com.toel.model.Product;
import com.toel.model.Account;
import com.toel.model.Bill;

public interface BillDetailRepository extends JpaRepository<BillDetail, Integer> {
	@Query("SELECT COALESCE(SUM( (bd.price * bd.quantity) * (1 - (bd.bill.discountRate.discount / 100.0)) - (bd.price * bd.quantity * "
			+ "COALESCE((SELECT COALESCE(vd.voucher.sale, 0)/ 100  FROM bd.bill.voucherDetails vd WHERE vd.bill = bd.bill and vd.voucher.typeVoucher.id=1),0))), 0)"
			+
			"FROM BillDetail bd " +
			"WHERE bd.product.account.id = :accountId " +
			"AND (bd.bill.finishAt BETWEEN :dateStart AND :dateEnd) ")
	Double calculateAverageBillByShop(@Param("accountId") Integer accountId,
			@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);

	@Query("SELECT COALESCE(SUM( (bd.product.price * bd.quantity) * "
			+ "( SELECT COALESCE(vd.voucher.sale, 0)/100  FROM bd.bill.voucherDetails vd WHERE vd.voucher.typeVoucher.id=2)),0)"
			+
			"FROM BillDetail bd " +
			"WHERE bd.product.account.id = :accountId " +
			"AND (bd.bill.finishAt BETWEEN :dateStart AND :dateEnd) ")
	Double calculateVoucherByShop_San(@Param("accountId") Integer accountId,
			@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);

	@Query("SELECT COALESCE(SUM(bd.product.price * (COALESCE(bd.flashSaleDetail.sale,0)/100)), 0) " +
			"FROM BillDetail bd " +
			"WHERE bd.product.account.id = :accountId " +
			"AND (bd.bill.finishAt BETWEEN :dateStart AND :dateEnd) ")
	Double calculateFlashSaleByShop_San(@Param("accountId") Integer accountId,
			@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);

	@Query("SELECT COALESCE(SUM((bd.product.price * bd.quantity) * (COALESCE(bd.bill.discountRate.discount / 100,0))),0) "
			+
			"FROM BillDetail bd " +
			"WHERE bd.product.account.id = :accountId " +
			"AND (bd.bill.finishAt BETWEEN :dateStart AND :dateEnd) ")
	Double calculateChietKhauByShop_San(@Param("accountId") Integer accountId,
			@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);

	@Query("SELECT  bd.quantity,  bd.bill.account.id, bd.product.id  FROM BillDetail bd  WHERE bd.bill.id = ?1")
	List<Object[]> getOriginBillsByBillId(Integer billId);

	// @Query(value = "SELECT user.id as userID, bills.id as billID,\r\n" + "
	// bills.totalPrice as billTotalPrice,\r\n"
	// + " COALESCE(bills.discountPrice, 0) as billDiscountPrice,\r\n"
	// + " bills.priceShipping as billTotalShippingPrice,\r\n"
	// + " bills.totalQuantity as billTotalQuantity, bills.address_id as
	// billAddressId,\r\n"
	// + " bills.orderstatus_id as billOrderStatusId, bills.createAt as
	// createdDatetime,\r\n"
	// + " bills.updateAt as updatedDatetime,\r\n"
	// + " COALESCE(bills.discountrate_id, 0) as billDiscountRate, products.id as
	// productId,\r\n"
	// + " products.name as productName, products.introduce as
	// productIntroduce,\r\n"
	// + " billdetails.quantity as productQuantity, billdetails.price as
	// productPrice,\r\n"
	// + " flashsaledetails.sale as productDiscountPrice,\r\n"
	// + " imageproducts.name as productImageURL, shop.id as
	// shopId,shop.shopName,\r\n"
	// + " shop.avatar as shopAvatar, user.fullname, user.phone\r\n"
	// + " FROM billdetails \r\n"
	// + " JOIN bills ON billdetails.bill_id = bills.id \r\n"
	// + " JOIN accounts user ON bills.account_id = user.id \r\n"
	// + " JOIN products ON billdetails.product_id = products.id \r\n"
	// + " JOIN accounts shop ON shop.id = products.account_id \r\n"
	// + " LEFT JOIN imageproducts ON imageproducts.product_id = products.id \r\n"
	// + " LEFT JOIN discountrates ON discountrates.id = bills.discountrate_id \r\n"
	// + " LEFT JOIN flashsaledetails ON flashsaledetails.id =
	// billdetails.flashsaledetail_id "
	// + " WHERE bills.id = :billId \r\n", nativeQuery = true)
	// List<Object[]> findBillDetailById(@Param("billId") Integer billId);

	@Query(value = "SELECT * FROM billdetails JOIN bills WHERE billdetails.id = :billDetailID AND billdetails.product_id = :product_id AND bills.account_id = :account_id ", nativeQuery = true)
	Object[] hasExistOrder(@Param("billDetailID") Integer billDetailID, @Param("product_id") Integer productId,
			@Param("account_id") Integer accountId);

	@Query("SELECT p FROM Product p WHERE p.id IN (SELECT DISTINCT bd.product.id FROM BillDetail bd WHERE bd.bill.finishAt BETWEEN :dateStart AND :dateEnd)")
	List<Product> selectAll(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);

	@Query("SELECT p FROM Product p WHERE p.id IN (Select DISTINCT bd.product.id FROM BillDetail bd WHERE bd.bill.finishAt BETWEEN :dateStart AND :dateEnd AND (:key iS NULL OR bd.product.name LIKE %:key% OR bd.product.introduce LIKE %:key% OR bd.product.writerName LIKE %:key% OR bd.product.publishingCompany LIKE %:key%)) ")
	List<Product> selectAllByFinishAt(@Param("key") String key, @Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd);

	@Query("Select COUNT(bd) FROM BillDetail bd WHERE bd.product = :product AND (bd.bill.finishAt BETWEEN :dateStart AND :dateEnd)")
	Integer calculateByFinishAtAndProduct(@Param("dateStart") Date dateStart,
			@Param("dateEnd") Date dateEnd,
			@Param("product") Product product);

	@Query(value = "SELECT CASE WHEN EXISTS " +
			"(SELECT billdetails.* FROM billdetails JOIN bills ON bills.id = billdetails.bill_id\r\n" +
			"WHERE billdetails.id = :billId\r\n" +
			"AND billdetails.product_id = :productId\r\n" +
			"AND bills.account_id = :accountId) \r\n " +
			"THEN 1 ELSE 0 END AS billDetailIsExisted ", nativeQuery = true)
	Integer billDetailIsExisted(@Param("billId") Integer bill_id, @Param("productId") Integer product_id,
			@Param("accountId") Integer account_id);

	@Query(value = "SELECT billdetails.id  \r\n" +
			"FROM  billdetails JOIN bills ON bills.id= billdetails.bill_id WHERE bills.account_id=:accountId \r\n" +
			"AND billdetails.product_id=:productId AND billdetails.bill_id=:billId", nativeQuery = true)
	Integer findBillDetailByProductIdAndAccountId(@Param("accountId") Integer accountId,
			@Param("productId") Integer productId,
			@Param("billId") Integer billId);

	List<BillDetail> findByProduct(Product product);

	List<BillDetail> findByProductIn(List<Product> product);

	List<BillDetail> findAllByBill(Bill bill);

	// findAllByBillIn

	List<BillDetail> findAllByBillIn(List<Bill> bill);
	// ByBillIn

	@Query("SELECT p.account.id FROM BillDetail bd JOIN bd.product p WHERE bd.bill.id = :billId")
	Integer findShopIdByBillId(@Param("billId") Integer billId);

	@Query("SELECT bd.product FROM BillDetail bd JOIN  bd.product p WHERE bd.bill.id = :billId")
	List<Product> findProductById(Integer billId);

	@Query("SELECT bd.price FROM BillDetail bd JOIN  bd.product p WHERE bd.id = ?1")
	Double findProductPriceByProduct(Integer billdetailId);

	@Query("SELECT bd.flashSaleDetail FROM BillDetail bd JOIN  bd.flashSaleDetail fl WHERE bd.id = ?1")
	FlashSaleDetail findFlashSaleByProduct(Integer billdetailId);

	@Query("SELECT bd FROM BillDetail bd WHERE bd.bill.id = :billId")
	List<BillDetail> findByBillId(Integer billId);

	// @Override
	// default List<BillDetail> findAllById(Iterable<Integer> ids) {
	// // TODO Auto-generated method stub
	// throw new UnsupportedOperationException("Unimplemented method
	// 'findAllById'");
	// }
}
