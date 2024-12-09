package com.toel.dto.admin.response;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toel.dto.admin.response.ThongKe.Response_TK_Seller;
import com.toel.dto.seller.response.Response_Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_ProductReport {
    Integer id;
    String title;
    boolean status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    LocalDate createAt;
    String content;
    Response_Product product;
    Response_Account account;
}
