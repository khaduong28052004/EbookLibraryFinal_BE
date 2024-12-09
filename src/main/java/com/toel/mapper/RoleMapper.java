package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.toel.dto.admin.request.Role.RequestRoleCreate;
import com.toel.dto.admin.request.Role.RequestRoleUpdate;
import com.toel.dto.admin.response.Response_Role;
import com.toel.model.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Response_Role tResponse_Role(Role role);

    Role createResponse_Role(RequestRoleCreate role);

    void updatRole(@MappingTarget Role role, RequestRoleUpdate requestRoleUpdate);
}
