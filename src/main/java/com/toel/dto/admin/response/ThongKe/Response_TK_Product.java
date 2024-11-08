package com.toel.dto.admin.response.ThongKe;

import java.util.Date;
import java.util.List;

import com.toel.dto.admin.response.Response_Account;
import com.toel.model.Category;
import com.toel.model.FlashSaleDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_TK_Product {
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
    Integer sumBill;
    Integer sumEvalue;
    Integer sumLike;
    Double avgStar;
    List<FlashSaleDetail> flashSaleDetails;
}
