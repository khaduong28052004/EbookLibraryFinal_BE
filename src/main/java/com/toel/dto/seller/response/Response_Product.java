package com.toel.dto.seller.response;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response_Product {
    Integer id;
    String name;
    String writerName;
    String publishingCompany;
    String introduce;
    double price;
    double sale;
    double weight;
    Integer quantity;
    boolean isDelete;
    boolean isActive;
    Response_Category category;
    List<Response_ImageProduct> imageProducts;
    List<byte[]> imagesByte;

}
