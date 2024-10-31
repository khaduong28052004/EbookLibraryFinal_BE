package com.toel.mapper;

import org.mapstruct.Mapper;

import com.toel.dto.admin.response.Response_Role;
import com.toel.model.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Response_Role tResponse_Role(Role role);
}
