package com.toel.dto.admin.response.ThongKe;

import java.util.Date;
import java.util.List;

import org.hibernate.internal.build.AllowPrintStacktrace;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toel.dto.admin.response.Response_AccountReport;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllowPrintStacktrace
@NoArgsConstructor
public class Response_TK_Seller {
    Integer id;
    String fullname;
    String email;
    String phone;
    String shopName;
    String avatar;
    boolean status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    Date createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    Date createAtSeller;
    Integer sumProduct;
    Integer sumFollower;
    Double avgStar;
    Integer sumReport;
    Double DoanhSo; // trừ chiết khấu
    Double DoanhThu; // không trừ chiết khấu
    List<Response_AccountReport> accountReports;
}
