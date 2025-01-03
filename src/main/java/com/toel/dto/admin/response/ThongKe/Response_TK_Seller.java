package com.toel.dto.admin.response.ThongKe;

import java.util.Date;

import org.hibernate.internal.build.AllowPrintStacktrace;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    Integer sumProduct;
    Integer sumFollower;
    Double avgStar;
    Integer sumReport;
    Double DoanhSo; // trừ chiết khấu
    Double DoanhThu; // không trừ chiết khấu
}
