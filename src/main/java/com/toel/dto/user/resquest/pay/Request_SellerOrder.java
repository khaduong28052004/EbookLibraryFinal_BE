package com.toel.dto.user.resquest.pay;

import java.util.List;

import com.toel.model.Voucher;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request_SellerOrder {

	private int id;
	List<Request_Cart> cart;
	private Voucher vouchers;

}
