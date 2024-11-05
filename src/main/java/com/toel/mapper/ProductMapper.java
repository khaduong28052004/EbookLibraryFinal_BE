package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.seller.request.Product.Request_ProductCreate;
import com.toel.dto.seller.request.Product.Request_ProductUpdate;
import com.toel.dto.seller.response.Response_Product;
import com.toel.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Response_Product response_Product(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "imageProducts", ignore = true)
    Product productCreate(Request_ProductCreate request_Product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "imageProducts", ignore = true)
    Product productUpdate(Request_ProductUpdate request_Product);
}
