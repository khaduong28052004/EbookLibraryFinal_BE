package com.toel.dto.user.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Bill_User {
	// private Integer billId;
	// private Integer userId;
	// private Double totalBillPrice;
	// private Double discountPricedBill; // sale product
	// private Double priceShippingBill;
	// private Integer totalBillQuantity;
	// private String address; // address Id
	// private String orderStatus; // orderstatus name
	// private Date createdDatetime;// create at,
	// private Date updatedDatetime;// update at
	// private String paymentMethod; // paymen method name
	// private Integer billDiscountRate; // sale theo discountrate
	// private List<Response_Bill_Product_User> products;

	private Integer billId; // b.id AS billId
	private Integer userId; // a.id AS userId
	private Double totalPriceBill; // b.totalPrice AS totalBillPrice
	private Double priceShippingBill; // b.priceShipping AS priceShippingBill
	private Integer totalQuantityBill; // b.totalQuantity AS totalBillQuantity
	private String orderStatus; // os.name AS orderStatus
	private Date createdDatetime; // b.createAt AS createdDatetime
	private Date updatedDatetime; // b.updateAt AS updatedDatetime
	private String paymentMethod; // b.paymentMethod.name AS paymentMethod
}