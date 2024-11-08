package com.toel.dto.admin.response.ThongKe;

import java.util.Date;

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
    Date birthday;
    String avatar;
    Date createAt;
    String phone;
    boolean status;
    Double avgdonhang;
    Integer sumDonHang;
    Double avgStar;
}
