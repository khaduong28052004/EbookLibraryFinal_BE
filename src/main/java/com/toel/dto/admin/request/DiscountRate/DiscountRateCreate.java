package com.toel.dto.admin.request.DiscountRate;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRateCreate {
    @NotNull(message = "Giảm giá không được để trống.")
    private Integer discount;
    @NotNull(message = "Ngày bắt đầu không được để trống.")
    private LocalDateTime dateStart;
    @NotNull(message = "Account không được để trống.")
    private Integer account;
}
