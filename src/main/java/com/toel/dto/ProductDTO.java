package com.toel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    // Integer productId;
    // String productName;
    // String productIntroduce;
    // Integer productQuantity;
    // Double productPrice;
    // Double productDiscountPrice;
    // String productImageURL;
    // Integer billDetailId;
    // Boolean isEvaluate;

    private Integer productId; // p.id AS productId
    private String productName; // p.name AS productName
    private String productIntroduce; // p.introduce AS productIntroduce
    private Integer quantity; // bd.quantity AS quantity
    private Double price; // bd.price AS price
    private Double flashSale; // fls.sale AS flashSale
    private String imageName; // ip.name AS imageName
    private Integer productDetailId; // p.id AS productDetailId
    private Integer shopId; // s.id AS shopId
    private String shopName; // s.shopName AS shopName
    private String shopAvatar; // s.avatar AS shopAvatar

}
