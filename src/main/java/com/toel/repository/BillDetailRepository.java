package com.toel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.dto.user.response.Response_BillDetail_User;
import com.toel.model.BillDetail;

public interface BillDetailRepository extends JpaRepository<BillDetail, Integer> {

	@Query("SELECT  bd.quantity,  bd.bill.account.id, bd.product.id  FROM BillDetail bd  WHERE bd.bill.id = ?1")
	List<Object[]> getOriginBillsByBillId(Integer billId);

	@Query(value = "SELECT  user.id as userID,  bills.id as billID,\r\n" + "    bills.totalPrice as billTotalPrice,\r\n"
			+ "    COALESCE(bills.discountPrice, 0) as billDiscountPrice,\r\n"
			+ "    bills.priceShipping as billTotalShippingPrice,\r\n"
			+ "    bills.totalQuantity as billTotalQuantity, bills.address_id as billAddressId,\r\n"
			+ "    bills.orderstatus_id as billOrderStatusId,  bills.createAt as createdDatetime,\r\n"
			+ "    bills.updateAt as updatedDatetime,\r\n"
			+ "    COALESCE(bills.discountrate_id, 0) as billDiscountRate, products.id as productId,\r\n"
			+ "    products.name as productName,   products.introduce as productIntroduce,\r\n"
			+ "    billdetails.quantity as productQuantity,    billdetails.price as productPrice,\r\n"
			+ "    billdetails.discountPrice as productDiscountPrice,\r\n"
			+ "    imageproducts.name as productImageURL, shop.id as shopId,shop.shopName,\r\n"
			+ "    shop.avatar as shopAvatar, user.fullname, user.phone\r\n" 
			+ "	   FROM billdetails \r\n"
			+ "	   JOIN bills ON billdetails.bill_id = bills.id \r\n"
			+ "	   JOIN accounts user ON bills.account_id = user.id \r\n"
			+ "	   JOIN products ON billdetails.product_id = products.id \r\n"
			+ "	   JOIN accounts shop ON shop.id = products.account_id \r\n"
			+ "	   LEFT JOIN imageproducts ON imageproducts.product_id = products.id \r\n"
			+ "	   LEFT JOIN discountrates ON discountrates.id = bills.discountrate_id \r\n"
			+ "	   WHERE bills.id = :billId \r\n", nativeQuery = true)
	List<Object[]> findBillDetailById(@Param("billId") Integer billId);

	@Query(value = "SELECT * FROM billdetails JOIN bills WHERE billdetails.id = :billDetailID AND billdetails.product_id = :product_id AND bills.account_id = :account_id ", nativeQuery = true)
	Object[] hasExistOrder(@Param("billDetailID") Integer billDetailID, @Param("product_id") Integer productId,
			@Param("account_id") Integer accountId);

}
