package com.toel.mapper;

import org.mapstruct.Mapper;

import com.toel.dto.seller.request.Category.Request_CategoryCreate;
import com.toel.dto.seller.request.Category.Request_CategoryUpdate;
import com.toel.dto.seller.response.Response_Category;
import com.toel.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Response_Category response_Category(Category category);

    Category categoryCreate(Request_CategoryCreate request_Category);

    Category categoryUpdate(Request_CategoryUpdate request_Category);
}
