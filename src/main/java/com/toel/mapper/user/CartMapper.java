package com.toel.mapper.user;

import org.mapstruct.Mapper;

import com.toel.dto.user.response.Response_Cart;
import com.toel.model.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
	Response_Cart response_Cart_MapperTo_Cart(Cart cart);
}
