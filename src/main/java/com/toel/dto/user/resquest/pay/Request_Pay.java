package com.toel.dto.user.resquest.pay;

import java.util.List;

import com.toel.model.Voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_Pay {
	List<Request_SellerOrder> datas;
	double sale;
	double service_fee;
	double total;
	Voucher voucher;
}
