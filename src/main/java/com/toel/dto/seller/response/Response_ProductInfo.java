package com.toel.dto.seller.response;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response_ProductInfo {
    Integer id;

    double price;

    double sale;

    double weight;

    String name;

    // String introduce;    

    String writerName;
    
    String publishingCompany;

    Date createAt;

    boolean isDelete;

    Integer quantity;
    Response_Account account;
    boolean isActive;
    // Response_Category category;
    List<Response_ImageProduct> imageProducts;
}


