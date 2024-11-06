package com.toel.mapper.user;

import org.mapstruct.Mapper;

import com.toel.dto.user.response.Response_Product;
import com.toel.model.Product;

@Mapper
public interface ProductMaper {
	Response_Product productToResponse_Product(Product product);
}
