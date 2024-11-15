package com.toel.dto.user.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Bill_User {
	Integer userID;
	Integer billID;
	Double billTotalPrice;
	Double billDiscountPrice;
	Double billTotalShippingPrice;
	Integer billTotalQuantity;
	String billAddress;
	Integer billOrderStatusId;
	String billOrderStatus;
	String billPaymentMethod;
	String createdDatetime;
	String updatedDatetime;
	Double billDiscountRate;
	Integer shopId;
	String shopName;
	String shopAvatar;
	List<Response_Bill_Product_User> products; 

}