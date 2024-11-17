package com.toel.dto.user.resquest.pay;

import com.toel.dto.user.response.Response_Product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request_Cart {

	private int id;
	private Integer quantity;
	private Response_Product product;

}
