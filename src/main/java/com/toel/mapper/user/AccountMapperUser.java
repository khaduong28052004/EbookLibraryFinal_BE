package com.toel.mapper.user;

import org.mapstruct.Mapper;

import com.toel.dto.user.response.Response_Seller;
import com.toel.dto.user.response.Response_SellerProductDetail;
import com.toel.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapperUser {
	Response_Seller Response_Seller_MapperTo_Account(Account account);

	Response_SellerProductDetail response_SellerProductDetailToAccount(Account account);

}
