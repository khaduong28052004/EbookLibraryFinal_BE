package com.toel.dto.admin.request.RolePermission;

import com.toel.dto.admin.response.Response_Permission;
import com.toel.dto.admin.response.Response_Role;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_RolePermissionCreate {
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer permission;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer role;
}
