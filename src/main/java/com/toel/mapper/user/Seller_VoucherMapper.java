package com.toel.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.seller.request.Request_Voucher;
import com.toel.dto.seller.response.Response_Voucher;
import com.toel.model.Voucher;

@Mapper(componentModel = "spring")
public interface Seller_VoucherMapper {

    Response_Voucher response_Voucher(Voucher voucher);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "typeVoucher", ignore = true)
    Voucher voucher(Request_Voucher request_Voucher);

}
