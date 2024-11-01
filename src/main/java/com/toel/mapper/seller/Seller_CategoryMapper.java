package com.toel.mapper.seller;

import org.mapstruct.Mapper;

import com.toel.dto.seller.request.Request_Category;
import com.toel.dto.seller.response.Response_Category;
import com.toel.model.Category;

@Mapper(componentModel = "spring")
public interface Seller_CategoryMapper {
    Response_Category response_Category(Category category);

    Category category(Request_Category request_Category);
}