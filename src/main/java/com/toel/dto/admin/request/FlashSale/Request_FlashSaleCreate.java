package com.toel.dto.admin.request.FlashSale;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request_FlashSaleCreate {
    @NotNull(message = "FIELD_REQUIRED")
    @Future(message = "FIELD_INVALID_FUTURE_DATE")
    private LocalDateTime dateStart;
    @NotNull(message = "FIELD_REQUIRED")
    @Future(message = "FIELD_INVALID_FUTURE_DATE")
    private LocalDateTime dateEnd;
    @NotNull(message = "FIELD_REQUIRED")
    private Integer account;
}
