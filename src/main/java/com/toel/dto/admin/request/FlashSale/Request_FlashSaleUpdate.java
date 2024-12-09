package com.toel.dto.admin.request.FlashSale;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request_FlashSaleUpdate {
  @NotNull(message = "FIELD_REQUIRED")
  @Min(value = 1, message = "FIELD_INVALID_ID")
  private Integer id;
  @NotNull(message = "FIELD_REQUIRED")
  private String title;
  @NotNull(message = "FIELD_REQUIRED")
  private LocalDateTime dateStart;
  @NotNull(message = "FIELD_REQUIRED")
  private LocalDateTime dateEnd;
  @NotNull(message = "FIELD_REQUIRED")
  private Integer account;
}
