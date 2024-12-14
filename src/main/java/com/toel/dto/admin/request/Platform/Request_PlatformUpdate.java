package com.toel.dto.admin.request.Platform;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_PlatformUpdate {
    @NotNull(message = "FIELD_REQUIRED")
    private Integer id;
    @NotBlank(message = "FIELD_REQUIRED")
    private String name;
    @NotBlank(message = "FIELD_REQUIRED")
    private String address;
    @NotBlank(message = "FIELD_REQUIRED")
    private String phone;
    @NotBlank(message = "FIELD_REQUIRED")
    private String email;
    @NotBlank(message = "FIELD_REQUIRED")
    private String policies;
}
