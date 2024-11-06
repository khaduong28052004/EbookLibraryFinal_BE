package com.toel.dto.user.response;

import java.util.Date;
import java.util.List;

import org.mapstruct.Mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response_BillDetail_User {
	Integer userID;
	Integer billID;
	Double billTotalPrice;
	Double billDiscountPrice;
	Double billTotalShippingPrice;
	Double billTempPrice;
	Integer billTotalQuantity;
	String billAddress;
	Integer billOrderStatusId;
	String billOrderStatus;
	String createdDatetime;
	String updatedDatetime;
	Double billDiscountRate;
	Integer shopId;
	String shopName;
	String shopAvatar;
	String userPhone;
	String userFullname;
	List<Response_Bill_Product_User> products;

}
