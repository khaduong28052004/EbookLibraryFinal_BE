package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.toel.dto.admin.request.Account.Request_AccountCreate;
import com.toel.dto.admin.request.Account.Request_AccountUpdate;
import com.toel.dto.admin.response.Response_Account;
import com.toel.dto.admin.response.ThongKe.Response_TKDT_Seller;
import com.toel.dto.admin.response.ThongKe.Response_TK_Account;
import com.toel.dto.admin.response.ThongKe.Response_TK_Seller;
import com.toel.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Response_Account toAccount(Account account);

    @Mapping(target = "role", ignore = true)
    Account toAccountCreate(Request_AccountCreate accountCreate);

    Account toAccountUpdate(Request_AccountUpdate accountUpdate);

    Response_TK_Seller to_TK_Seller(Account account);

    Response_TKDT_Seller tResponse_TKDT_Seller(Account account);

    Response_TK_Account tResponse_TK_Account(Account account);
}
