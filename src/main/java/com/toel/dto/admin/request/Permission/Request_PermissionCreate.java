package com.toel.dto.admin.request.Permission;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_PermissionCreate {
    String description;
    @NotBlank(message = "FIELD_REQUIRED")
    String cotSlug;
    Integer role;
}
