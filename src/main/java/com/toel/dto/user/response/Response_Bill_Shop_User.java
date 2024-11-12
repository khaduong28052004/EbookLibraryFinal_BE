package com.toel.dto.user.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Bill_Shop_User {
	Integer shopId;
	String shopName;
	String shopAvatar;
	List<Response_Bill_User> bills;
}
