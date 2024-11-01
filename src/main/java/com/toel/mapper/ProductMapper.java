package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.seller.request.Request_Product;
import com.toel.dto.seller.response.Response_Product;
import com.toel.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Response_Product response_Product(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "account", ignore = true)
    Product product(Request_Product request_Product);
}
