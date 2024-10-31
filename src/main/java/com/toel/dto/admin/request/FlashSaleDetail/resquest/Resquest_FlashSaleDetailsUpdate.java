package com.toel.dto.admin.request.FlashSaleDetail.resquest;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Resquest_FlashSaleDetailsUpdate {
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer id;
    @NotNull(message = "FIELD_REQUIRED")
    LocalDateTime dateStart;
    @NotNull(message = "FIELD_REQUIRED")
    LocalDateTime dateEnd;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer account;
}
