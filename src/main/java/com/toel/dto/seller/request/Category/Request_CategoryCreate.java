package com.toel.dto.seller.request.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_CategoryCreate {
    @NotBlank(message = "FIELD_REQUIRED")
    String name;
    @NotNull(message = "FIELD_REQUIRED")
    Integer idParent;
}
