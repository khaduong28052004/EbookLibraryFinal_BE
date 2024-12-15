package com.toel.dto.admin.request.ImagePlaform;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_ImagePlaformCreate {
    @NotBlank(message = "FIELD_REQUIRED")
    String url;
    @NotNull(message = "FIELD_REQUIRED")
    Integer categoryImage;
}
