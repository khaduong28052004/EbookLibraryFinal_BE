package com.toel.dto.admin.response;

import java.util.Date;
import java.util.List;

import com.toel.dto.seller.response.Response_ImageProduct;
import com.toel.model.Category;
import com.toel.model.ImageProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_ProductListFlashSale {
    Integer id;
    double price;
    double sale;
    String name;
    String introduce;
    String writerName;
    String publishingCompany;
    Date createAt;
    boolean isDelete;
    Integer quantity;
    boolean isActive;
    Response_Account account;
    Category category;
    List<Response_ImageProduct> imageProducts;
    // List<Response_FlashSaleDetail> flashSaleDetails;
    Integer sumBaoCao;
    double weight;
}
