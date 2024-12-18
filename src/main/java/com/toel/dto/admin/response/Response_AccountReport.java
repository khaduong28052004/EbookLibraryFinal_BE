package com.toel.dto.admin.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toel.dto.admin.response.ThongKe.Response_TK_Seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_AccountReport {
    Integer id;
    String title;
    boolean status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    LocalDate createAt;
    String content;
    Response_TK_Seller shop;
    Response_Account account;
    List<Response_ImageAccountReport> imageAccountReports;

}
