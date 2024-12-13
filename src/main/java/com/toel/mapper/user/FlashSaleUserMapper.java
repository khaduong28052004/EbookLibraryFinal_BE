package com.toel.mapper.user;

import org.mapstruct.Mapper;

import com.toel.dto.user.response.Response_FlashSale;
import com.toel.model.FlashSale;

@Mapper(componentModel = "spring")
public interface FlashSaleUserMapper {

	Response_FlashSale mapper_Response_FlashSale(FlashSale flashSale);
}
