package com.toel.dto.user.response;

import java.util.Date;

import org.mapstruct.Mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Mapper(componentModel = "spring")
public class Response_Order {
	Integer orderID;
	Double orderTotalPrice;
	Double orderDiscountPrice;
	Double orderTotalShippingPrice; 
	Integer orderTotalQuantity;
	String orderAddress;
	Integer orderOrderStatusId;
	Date createdDatetime;
	Date updatedDatetime;
	Integer orderChietKhauId;
	Integer shopId;
	String shopName;
	String shopImageURL;
	Integer productId;
	String productName;
	String productImageURL;
	String productIntroduce;
	Integer productQuantity;
	Double productPrice;
	Double productDiscountPrice;
}
