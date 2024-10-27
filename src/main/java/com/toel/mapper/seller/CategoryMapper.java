package com.toel.mapper.seller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.seller.request.Request_Category;
import com.toel.dto.seller.response.Response_Category;
import com.toel.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Response_Category response_Category(Category category);

    Category category (Request_Category request_Category);
}
