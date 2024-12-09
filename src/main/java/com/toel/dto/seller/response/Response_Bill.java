package com.toel.dto.seller.response;

import java.util.Date;
import java.util.List;

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
    List<Response_VoucherDetail> voucherDetails;
    List<Response_BillDetail> billDetails;
    Response_OrderStatus orderStatus;
    Response_Account account;
    Response_PaymentMethod paymentMethod;
    Response_Address address;
}
