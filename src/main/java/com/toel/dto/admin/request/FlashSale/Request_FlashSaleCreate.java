package com.toel.dto.admin.request.FlashSale;

import java.time.LocalDateTime;
import java.util.Date;

import com.toel.dto.admin.request.Account.Request_AccountCreate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request_FlashSaleCreate {
    @NotNull(message = "FIELD_REQUIRED")
    private LocalDateTime dateStart;
    @NotNull(message = "FIELD_REQUIRED")
    private LocalDateTime dateEnd;
    @NotNull(message = "FIELD_REQUIRED")
    private Integer account;
}
