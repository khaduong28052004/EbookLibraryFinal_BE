package com.toel.mapper;

import org.mapstruct.Mapper;
import com.toel.dto.admin.response.Response_DiscountRate;
import com.toel.model.DiscountRate;

@Mapper(componentModel = "spring")
public interface DiscountRateMapper {
    Response_DiscountRate tochChietKhauResponse(DiscountRate discountRate);
}
