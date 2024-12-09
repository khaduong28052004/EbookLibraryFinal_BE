package com.toel.dto.user.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response_Bill_Product_User {
	private Integer billId; // b.id AS billId
	private Integer userId; // a.id AS userId
	private Double totalPriceBill; // b.totalPrice AS totalBillPrice
	private Double priceShippingBill; // b.priceShipping AS priceShippingBill
	private Integer totalQuantityBill; // b.totalQuantity AS totalBillQuantity
	private String orderStatus; // os.name AS orderStatus
	private Date createdDatetime; // b.createAt AS createdDatetime
	private Date updatedDatetime; // b.updateAt AS updatedDatetime
}
