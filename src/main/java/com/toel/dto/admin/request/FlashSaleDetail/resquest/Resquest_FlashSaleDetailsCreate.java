package com.toel.dto.admin.request.FlashSaleDetail.resquest;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resquest_FlashSaleDetailsCreate {
    @NotNull(message = "FIELD_REQUIRED")
    LocalDateTime dateStart;
    @NotNull(message = "FIELD_REQUIRED")
    LocalDateTime dateEnd;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer account;
}
