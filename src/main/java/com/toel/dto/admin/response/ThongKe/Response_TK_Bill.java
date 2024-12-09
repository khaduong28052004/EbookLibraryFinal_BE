package com.toel.dto.admin.response.ThongKe;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toel.dto.admin.response.Response_Account;
import com.toel.model.DiscountRate;
import com.toel.dto.seller.response.Response_Address;
import com.toel.dto.seller.response.Response_BillDetail;
import com.toel.dto.seller.response.Response_OrderStatus;
import com.toel.dto.seller.response.Response_PaymentMethod;
import com.toel.dto.seller.response.Response_VoucherDetail;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_TK_Bill {
    private int id;
    private double priceShipping;
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
    private Response_Account account;
    private List<Response_VoucherDetail> voucherDetails;
    private List<Response_BillDetail> billDetails;
    private Response_OrderStatus orderStatus;
    private Response_PaymentMethod paymentMethod;
    private Response_Address address;
}
