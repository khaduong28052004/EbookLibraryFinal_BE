package com.toel.dto.admin.response;

import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_DiscountRate {
    private int id;
    private LocalDateTime dateDelete;
    private int discount;
    private LocalDateTime dateStart;
    private LocalDateTime dateInsert;
    private Response_Account account;
}
