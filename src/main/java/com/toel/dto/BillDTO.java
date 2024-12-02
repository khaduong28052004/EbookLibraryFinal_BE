package com.toel.dto;

import java.util.Date;
import java.util.List;

import com.toel.dto.user.response.Response_Bill_Product_User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDTO {
    private Integer billId; // b.id AS billId
    private Integer userId; // a.id AS userId
    private Double totalPriceBill; // b.totalPrice AS totalBillPrice
    private Double priceShippingBill; // b.priceShipping AS priceShippingBill
    private Integer totalQuantityBill; // b.totalQuantity AS totalBillQuantity
    private String orderStatus; // os.name AS orderStatus
    private Date createdDatetime; // b.createAt AS createdDatetime
    private Date updatedDatetime; // b.updateAt AS updatedDatetime
    private String paymentMethod;
}
