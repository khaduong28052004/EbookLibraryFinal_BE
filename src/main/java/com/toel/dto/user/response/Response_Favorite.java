package com.toel.dto.user.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Favorite {

	Integer id;
	Date createAt;
	Response_Product product;

}
