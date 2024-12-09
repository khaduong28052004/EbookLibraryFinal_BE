package com.toel.dto.user.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_InfoBill_Billdetail_DTO {
    private Integer billId;
    private Integer accountId;
    private String billAddressId;
    private Double billDiscountPrice;
    private Integer billDiscountRate;
    private Integer billOrderStatusId;
    private Integer billPaymentMethodId;
    private Double billPriceShippng;
    private Double billTotalPrice;
    private Integer billTotalQuantity;
    private Date billCreatedAt;
    private Date billFinishAt;
    private Date billUpdateAt;
}
