package com.toel.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.toel.dto.admin.request.FlashSale.Request_FlashSaleCreate;
import com.toel.dto.admin.request.FlashSale.Request_FlashSaleUpdate;
import com.toel.dto.admin.response.Response_FlashSale;
import com.toel.model.FlashSale;

@Mapper(componentModel = "spring")
public interface Admin_FlashSaleMapper {
    @Mapping(target = "account", ignore = true)
    Response_FlashSale tResponse_FlashSale(FlashSale flashSale);

    @Mapping(target = "account", ignore = true)
    FlashSale toFlashSaleCreate(Request_FlashSaleCreate flashSaleCreate);

    @Mapping(target = "account", ignore = true)
    void toFlashSaleUpdate(@MappingTarget FlashSale flashSale, Request_FlashSaleUpdate flashSaleUpdate);
}
