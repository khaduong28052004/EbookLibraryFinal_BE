package com.toel.dto.admin.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Permission {
    Integer id;
    String description;
    String cotSlug;
    // List<Response_RolePermission> rolePermissions;
}
