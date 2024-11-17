package com.toel.dto.admin.response;

import com.toel.dto.seller.response.Response_Product;
import com.toel.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_FlashSaleDetail {
    Integer id;
    Integer quantity;
    Integer sale;
    Response_Product product;
    Response_FlashSale flashSale;
}
