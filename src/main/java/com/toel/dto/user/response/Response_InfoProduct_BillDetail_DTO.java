package com.toel.dto.user.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_InfoProduct_BillDetail_DTO {
    // private Integer productId;
    // private String prodcutName;
    // private String productIntroduce;
    // private Integer productQuantity;
    // private Double productPrice;
    // private Double productDiscountPrice;
    // private String productImageUrl;

    private int accountId;
    private int categoryId;
    private int id;
    private boolean isActive;
    private boolean isDelete;
    private double price;
    private int quantity;
    private double sale;
    private double weight;
    private Date createAt;
    private String introduce;
    private String name;
    private String publishingCompany;
    private String writerName;
}
