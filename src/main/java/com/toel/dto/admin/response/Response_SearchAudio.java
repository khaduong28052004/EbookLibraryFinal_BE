package com.toel.dto.admin.response;

import java.util.Date;
import java.util.List;

import com.toel.dto.user.response.Response_Evalue;
import com.toel.dto.user.response.Response_ImageProduct;
import com.toel.model.Account;
import com.toel.model.Category;
import com.toel.model.FlashSaleDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_SearchAudio {
    Integer id;
    double price;
    double sale;
    String name;
    String introduce;
    String writerName;
    String publishingCompany;
    Date createAt;
    Integer quantity;
    List<Response_Evalue> evalues;
    Category category;
    double star;
    float quantityEvalue;
    List<Response_ImageProduct> imageProducts;
    Account account;
    double weight;
    FlashSaleDetail flashSaleDetail = null;

    Integer sumBill;
    Integer sumEvalue;
    Integer sumLike;
    Double avgStar;
}
