package com.toel.dto.admin.request.Permission;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Requset_PermissionUpdate {
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer id;
    String description;
    @NotBlank(message = "FIELD_REQUIRED")
    String cotSlug;
}
