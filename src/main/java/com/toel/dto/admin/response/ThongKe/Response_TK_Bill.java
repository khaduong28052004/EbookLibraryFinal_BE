package com.toel.dto.admin.response.ThongKe;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_TK_Bill {
    private int id;
    private double totalPrice;
    private double discountPrice;
    private int totalQuantity;
    private double phi;
    private double loinhuan;
}
