package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.seller.request.Category.Request_CategoryCreate;
import com.toel.dto.seller.request.Category.Request_CategoryUpdate;
import com.toel.dto.seller.response.Response_Category;
import com.toel.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Response_Category response_Category(Category category);

    @Mapping(target = "account", ignore = true)
    Category categoryCreate(Request_CategoryCreate request_Category);

    @Mapping(target = "account", ignore = true)
    Category categoryUpdate(Request_CategoryUpdate request_Category);
}
