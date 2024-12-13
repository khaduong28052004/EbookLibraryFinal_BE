package com.toel.mapper.user;

import org.mapstruct.Mapper;

import com.toel.dto.user.response.Response_Favorite;
import com.toel.model.Like;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
	Response_Favorite response_FavoriteToLike(Like like);
}
