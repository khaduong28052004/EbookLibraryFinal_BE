package com.toel.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Response_FlashSaleDetail {

	Integer id;

	Integer quantity;

	Integer sale;

	double maxDiscount;

	Response_Product product;

}
