package com.toel.dto.admin.request.RolePermission;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_RolePermissionCreate {
    @NotNull(message = "FIELD_REQUIRED")
    @NotEmpty(message = "FIELD_REQUIRED")
    List<Integer> permission;
    @NotBlank(message = "FIELD_REQUIRED")
    String role;
}
