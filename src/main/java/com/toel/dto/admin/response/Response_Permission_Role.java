package com.toel.dto.admin.response;

import java.util.List;

import com.toel.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Permission_Role {
    Integer id;
    String description;
    String cotSlug;
    List<Role> rolePermissions;
}
