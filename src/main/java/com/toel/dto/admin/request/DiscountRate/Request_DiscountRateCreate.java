package com.toel.dto.admin.request.DiscountRate;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_DiscountRateCreate {
    @NotNull(message = "Giảm giá không được để trống.")
    private Integer discount;
    @NotNull(message = "Ngày bắt đầu không được để trống.")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateStart;
    @NotNull(message = "Account không được để trống.")
    private Integer account;
}
