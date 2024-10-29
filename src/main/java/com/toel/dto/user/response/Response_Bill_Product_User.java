package com.toel.dto.user.response;

import java.util.Date;
import java.util.List;

import org.mapstruct.Mapper;

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
@Mapper(componentModel = "spring")
public class Response_Bill_Product_User {
	Integer productId;
	String productName;
	String productIntroduce;
	Integer productQuantity;
	Double productPrice;
	Double productDiscountPrice;
	String productImageURL;
	Boolean isEvaluate;

}
