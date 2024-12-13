package com.toel.mapper;

import org.mapstruct.Mapper;

import com.toel.dto.admin.response.Response_Log;
import com.toel.model.Log;

@Mapper(componentModel = "spring")
public interface LogMapper {
    Response_Log tResponse_Log(Log list);
}
