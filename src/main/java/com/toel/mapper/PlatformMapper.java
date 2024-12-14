package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.toel.dto.admin.response.Response_Platform;
import com.toel.model.Platform;
import com.toel.dto.admin.request.Platform.Request_PlatformUpdate;

@Mapper(componentModel = "spring")
public interface PlatformMapper {
    Response_Platform tResponse_Platform(Platform platform);

    void Request_PlatformUpdate(@MappingTarget Platform platform, Request_PlatformUpdate platformUpdate);
}
