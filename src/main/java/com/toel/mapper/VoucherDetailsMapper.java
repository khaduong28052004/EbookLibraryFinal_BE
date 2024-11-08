package com.toel.mapper;

import org.mapstruct.Mapper;

import com.toel.dto.admin.response.Response_VoucherDetatils;
import com.toel.model.VoucherDetail;

@Mapper(componentModel = "spring")
public interface VoucherDetailsMapper {
    Response_VoucherDetatils toResponse_VoucherDetatils(VoucherDetail voucherDetail);
}
