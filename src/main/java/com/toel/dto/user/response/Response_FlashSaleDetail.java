package com.toel.dto.user.response;

import com.toel.model.Product;

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

	Response_Product product;

}
