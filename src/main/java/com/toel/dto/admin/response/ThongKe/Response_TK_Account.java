package com.toel.dto.admin.response.ThongKe;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_TK_Account {
    Integer id;
    String username;
    String fullname;
    Boolean gender;
    String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    Date birthday;
    String avatar;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    Date createAt;
    String phone;
    boolean status;
    Double avgdonhang;
    Integer sumDonHang;
    Double avgStar;
}
