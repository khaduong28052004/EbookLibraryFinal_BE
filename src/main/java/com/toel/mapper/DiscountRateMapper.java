package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.admin.request.DiscountRate.Request_DiscountRateCreate;
import com.toel.dto.admin.response.Response_DiscountRate;
import com.toel.model.DiscountRate;

@Mapper(componentModel = "spring")
public interface DiscountRateMapper {
    Response_DiscountRate tochChietKhauResponse(DiscountRate discountRate);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "dateInsert", ignore = true)
    DiscountRate toDiscountRateCreate(Request_DiscountRateCreate dRateCreate);
}
