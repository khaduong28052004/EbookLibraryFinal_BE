package com.toel.mapper.admin;

import org.mapstruct.Mapper;

import com.toel.dto.admin.response.Response_Role;
import com.toel.model.Role;

@Mapper(componentModel = "spring")
public interface Admin_RoleMapper {
    Response_Role tResponse_Role(Role role);
}
