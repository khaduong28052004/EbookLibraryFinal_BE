package com.toel.dto.seller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_Bill {
    @NotNull(message = "FIELD_REQUIRED")
    Integer id;
    @NotNull(message = "FIELD_REQUIRED")
    Integer orderStatus;
}
