package com.toel.dto.seller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Product {
    Integer id;
    String name;
    double price;
    double sale;
    Integer quantity;
    boolean isDelete;
    boolean isActive;
    Response_Category category;
}