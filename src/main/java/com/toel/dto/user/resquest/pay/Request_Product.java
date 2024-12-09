package com.toel.dto.user.resquest.pay;

import com.toel.model.FlashSaleDetail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request_Product {
	private int id;
	FlashSaleDetail flashSaleDetail;
}
