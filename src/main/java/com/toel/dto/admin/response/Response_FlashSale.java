package com.toel.dto.admin.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_FlashSale {
	Integer id;
	Date dateStart;
	Date dateEnd;
	boolean isDelete;
	Response_Account account;
}
