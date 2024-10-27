package com.toel.dto.admin.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_Bill {
    private int id;
    private double totalPrice;
    private double discountPrice;
    private int totalQuantity;
}
