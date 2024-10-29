package com.toel.dto.admin.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_RolePermission {
    Integer id;
    Response_Permission permission;
    Response_Role role;
}
