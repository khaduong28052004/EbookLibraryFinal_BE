package com.toel.dto.seller.response;

import java.util.Date;

import com.toel.dto.seller.request.Request_Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Bill {
    Integer id;
    double totalPrice;
    double discountPrice;
    int totalQuantity;
    double priceShipping;
    Date finishAt;
    Date createAt;
    Date updateAt;
    Response_OrderStatus orderStatus;
    Request_Account account;
    Response_PaymentMethod paymentMethod;
    Response_Address address;
}
