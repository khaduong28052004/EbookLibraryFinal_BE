package com.toel.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response_BillDetail_User {
	private Integer billId;
	private Integer userId;
	private Double billTotalPrice;
	private Double billDiscountPrice;
	private Double billPriceShipping;
	private Integer billTotalQuantity;
	private String orderStatus;
	private Integer orderStatusId;
	private String createdDatetime;
	private String updatedDatetime;
	private Double billDiscountRate;
	private Double productTempPrice;
	private Double billShippingPrice;
	private String userPhone;
	private String userFullname;
	private String userAddress;
}
