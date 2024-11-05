package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.seller.request.Voucher.Request_VoucherCreate;
import com.toel.dto.seller.request.Voucher.Request_VoucherUpdate;
import com.toel.dto.seller.response.Response_Voucher;
import com.toel.model.Voucher;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    Response_Voucher response_Voucher(Voucher voucher);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "typeVoucher", ignore = true)
    Voucher voucherCreate(Request_VoucherCreate request_Voucher);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "typeVoucher", ignore = true)
    Voucher voucherUpdate(Request_VoucherUpdate request_Voucher);

}
