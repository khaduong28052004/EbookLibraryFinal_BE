package com.toel.mapper;

import org.mapstruct.Mapper;

import com.toel.dto.admin.request.Account.Request_AccountCreate;
import com.toel.dto.admin.response.Response_Account;
import com.toel.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Response_Account toAccount(Account account);

    Account toAccountCreate(Request_AccountCreate accountCreate);
}
