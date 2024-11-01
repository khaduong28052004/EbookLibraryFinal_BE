package com.toel.mapper.user;

import org.mapstruct.Mapper;

import com.toel.dto.seller.request.Request_Account;
import com.toel.dto.seller.response.Response_Account;
import com.toel.model.Account;

@Mapper(componentModel = "spring")
public interface Seller_ShopMapper {
    Response_Account response_Account(Account account);

    Account account(Request_Account request_Account);
}
