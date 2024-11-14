package com.toel.dto.user.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Cart {
	Integer id;
	Integer quantity;
	Response_Product product;
}
