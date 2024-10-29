package com.toel.mapper.admin;

import org.mapstruct.Mapper;

import com.toel.dto.admin.response.Response_RolePermission;
import com.toel.model.RolePermission;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {
    Response_RolePermission toRolePermission(RolePermission entity);
}
