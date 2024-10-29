package com.toel.mapper.seller;

import org.mapstruct.Mapper;

import com.toel.dto.seller.request.Request_Evalue;
import com.toel.dto.seller.response.Response_Evalue;
import com.toel.model.Evalue;

@Mapper(componentModel = "spring")
public interface EvalueMapper {
    Response_Evalue response_Evalue(Evalue evalue);

    Evalue evalue(Request_Evalue request_Evalue);
}
