package com.toel.dto.seller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_Account {
    @NotNull(message = "FIELD_REQUIRED")
    Integer id;
    @NotBlank(message = "FIELD_REQUIRED")
    String avatar;
    @NotBlank(message = "FIELD_REQUIRED")
    String background;
    @NotBlank(message = "FIELD_REQUIRED")
    String shopName;
}
