package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.seller.request.Request_Evalue;
import com.toel.dto.seller.response.Response_Evalue;
import com.toel.model.Evalue;

@Mapper(componentModel = "spring")
public interface EvalueMapper {
    Response_Evalue response_Evalue(Evalue evalue);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "product", ignore = true)
    Evalue evalue(Request_Evalue request_Evalue);
}
