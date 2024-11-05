package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.toel.dto.seller.request.Voucher.Request_VoucherCreate;
import com.toel.dto.seller.request.Voucher.Request_VoucherUpdate;
// import com.toel.dto.admin.request.voucher.Request_VoucherCreate;
// import com.toel.dto.admin.request.voucher.Request_VoucherUpdate;

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

    com.toel.dto.admin.response.Response_Voucher toAdmiResponse_Voucher(Voucher voucher);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "typeVoucher", ignore = true)
    Voucher toAdmin_voucherCreate(com.toel.dto.admin.request.voucher.Request_VoucherCreate request_Voucher);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "typeVoucher", ignore = true)
    void toAdmin_voucherUpdate(@MappingTarget Voucher voucher,
            com.toel.dto.admin.request.voucher.Request_VoucherUpdate voucherUpdate);
}
