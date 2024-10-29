package com.toel.dto.admin.request.DiscountRate;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_DiscountRateCreate {
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    private Integer discount;
    @NotNull(message = "FIELD_REQUIRED")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateStart;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    private Integer account;
}
