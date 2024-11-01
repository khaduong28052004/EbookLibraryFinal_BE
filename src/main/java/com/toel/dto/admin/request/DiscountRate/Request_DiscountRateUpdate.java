package com.toel.dto.admin.request.DiscountRate;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_DiscountRateUpdate {
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_MIN_VALUE")
    private Integer id;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_MIN_VALUE")
    private Integer discount;
    @NotNull(message = "FIELD_REQUIRED")
    private LocalDateTime dateStart;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    private Integer account;
}
