package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.toel.dto.admin.request.voucher.Request_VoucherCreate;
import com.toel.dto.admin.request.voucher.Request_VoucherUpdate;
import com.toel.dto.seller.request.Request_Voucher;
import com.toel.dto.seller.response.Response_Voucher;
import com.toel.model.Voucher;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    Response_Voucher response_Voucher(Voucher voucher);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "typeVoucher", ignore = true)
    Voucher voucher(Request_Voucher request_Voucher);

    com.toel.dto.admin.response.Response_Voucher toAdmiResponse_Voucher(Voucher voucher);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "typeVoucher", ignore = true)
    Voucher toAdmin_voucherCreate(Request_VoucherCreate request_Voucher);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "typeVoucher", ignore = true)
    void toAdmin_voucherUpdate(@MappingTarget Voucher voucher, Request_VoucherUpdate voucherUpdate);
}
