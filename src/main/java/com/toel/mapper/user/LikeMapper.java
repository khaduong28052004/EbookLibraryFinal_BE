package com.toel.mapper.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.seller.response.Response_Like;
import com.toel.model.Like;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(source = "product.id", target = "product")
    @Mapping(source = "account.id", target = "account")
    Response_Like mapToResponseLike(Like like);

    List<Response_Like> mapToResponseLikeList(List<Like> likes);
}

