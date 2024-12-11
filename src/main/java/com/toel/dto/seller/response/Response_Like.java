package com.toel.dto.seller.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Like {
	Integer id;
	boolean status;
	Date createAt;
	Integer product;
	Integer account;
}
