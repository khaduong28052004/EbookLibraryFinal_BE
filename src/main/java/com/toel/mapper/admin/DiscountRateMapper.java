package com.toel.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.toel.dto.admin.request.DiscountRate.DiscountRateCreate;
import com.toel.dto.admin.response.Response_DiscountRate;
import com.toel.model.DiscountRate;

@Mapper(componentModel = "spring")
public interface DiscountRateMapper {
    Response_DiscountRate tochChietKhauResponse(DiscountRate discountRate);

    @Mapping(target = "account", ignore = true)
    DiscountRate toDiscountRateCreate(DiscountRateCreate dRateCreate);
}
