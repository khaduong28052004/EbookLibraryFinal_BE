package com.toel.dto.admin.request.RolePermission;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_RolePermissionCreate {
    @NotNull(message = "FIELD_REQUIRED")
    Integer permission;
    @NotBlank(message = "FIELD_REQUIRED")
    String role;
}
