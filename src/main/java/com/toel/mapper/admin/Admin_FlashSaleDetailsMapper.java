package com.toel.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.toel.dto.admin.request.FlashSale.Request_FlashSaleUpdate;
import com.toel.dto.admin.request.FlashSaleDetail.Resquest_FlashSaleDetailsCreate;
import com.toel.dto.admin.request.FlashSaleDetail.Resquest_FlashSaleDetailsUpdate;
import com.toel.dto.admin.response.Response_FlashSaleDetail;
import com.toel.model.FlashSaleDetail;

@Mapper(componentModel = "spring")
public interface Admin_FlashSaleDetailsMapper {
    Response_FlashSaleDetail toFlashSaleDetail(FlashSaleDetail flashSaleDetail);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "flashSale", ignore = true)
    FlashSaleDetail toFlashSaleDetailCreate(Resquest_FlashSaleDetailsCreate flashSaleDetailsCreate);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "flashSale", ignore = true)
    void toFlashSaleDetailUpdate(@MappingTarget FlashSaleDetail flashSaleDetail,
            Resquest_FlashSaleDetailsUpdate flashSaleDetailsUpdate);
}
