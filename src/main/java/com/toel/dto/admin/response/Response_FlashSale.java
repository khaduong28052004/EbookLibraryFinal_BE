package com.toel.dto.admin.response;

import java.util.Date;
import java.util.List;

import com.toel.dto.admin.request.Account.Request_AccountCreate;
import com.toel.model.FlashSaleDetail;

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
