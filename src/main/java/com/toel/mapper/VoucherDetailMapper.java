package com.toel.mapper;

import org.mapstruct.Mapper;

import com.toel.dto.seller.response.Response_VoucherDetail;
import com.toel.model.VoucherDetail;

@Mapper(componentModel = "spring")
public interface VoucherDetailMapper {
    Response_VoucherDetail response_VoucherDetail (VoucherDetail voucherDetail);
}
