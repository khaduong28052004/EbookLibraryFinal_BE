package com.toel.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.toel.dto.admin.request.Permission.Request_PermissionCreate;
import com.toel.dto.admin.request.Permission.Requset_PermissionUpdate;
import com.toel.dto.admin.response.Response_Permission;
import com.toel.model.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Response_Permission toPermission(Permission permission);

    Permission toPermissionCreate(Request_PermissionCreate entity);

    void toPermissionUpdate(@MappingTarget Permission permission, Requset_PermissionUpdate request);
}
