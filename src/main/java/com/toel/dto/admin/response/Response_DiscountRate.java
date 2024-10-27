package com.toel.dto.admin.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_DiscountRate {
    private int id;
    private LocalDateTime dateDelete;
    private int discount;
    private LocalDateTime dateStart;
    private LocalDateTime dateCreate;
    private Response_Account account;
    private List<Response_Bill> bills;
}
