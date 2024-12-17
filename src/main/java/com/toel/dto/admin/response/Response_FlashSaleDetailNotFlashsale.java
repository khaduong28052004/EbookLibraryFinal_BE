package com.toel.dto.admin.response;

import com.toel.dto.seller.response.Response_Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_FlashSaleDetailNotFlashsale {
    Integer id;
    Integer quantity;
    Integer sale;
    Response_Product product;
}
