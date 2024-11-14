package com.toel.dto.admin.response.ThongKe;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toel.dto.admin.response.Response_Account;
import com.toel.model.Address;
import com.toel.model.DiscountRate;
import com.toel.model.OrderStatus;
import com.toel.model.PaymentMethod;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_TK_Bill {
    private int id;
    private double totalPrice;
    private double discountPrice;
    private int totalQuantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    private Date finishAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    private Date createAt;
    private double phi;
    private double loinhuan;
    private DiscountRate discountRate;
    private OrderStatus orderStatus;
    private Address address;
    private PaymentMethod paymentMethod;
    private Response_Account account;
}
