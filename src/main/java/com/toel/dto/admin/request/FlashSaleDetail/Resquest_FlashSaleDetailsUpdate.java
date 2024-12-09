package com.toel.dto.admin.request.FlashSaleDetail;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resquest_FlashSaleDetailsUpdate {
    @NotNull(message = "FIELD_REQUIRED")
	@Min(value = 1, message = "FIELD_INVALID_ID")
	Integer id;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_MIN_VALUE")
    Integer quantity;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_MIN_VALUE")
    Integer sale;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer product;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer flashSale;
}
