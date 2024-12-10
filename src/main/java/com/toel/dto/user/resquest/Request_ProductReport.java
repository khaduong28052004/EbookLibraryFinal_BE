package com.toel.dto.user.resquest;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request_ProductReport {
	String title;
	String content;
	Integer id_user;
	Integer id_product;
}