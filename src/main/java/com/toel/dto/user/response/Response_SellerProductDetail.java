package com.toel.dto.user.response;

import java.util.Date;
import java.util.List;

import com.toel.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_SellerProductDetail {
	Integer id;

	String username;

	String password;

	String avatar;

	String fullname;

	boolean gender;

	String email;

	Date birthday;

	String phone;

	String background;

	String shopName;

	boolean status;

	Date createAt;

	Date createAtSeller;

	List<Response_Product> products;
	
	Integer totalEvalue;
	
	double totalStar;
}
