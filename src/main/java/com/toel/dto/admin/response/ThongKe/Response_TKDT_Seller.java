package com.toel.dto.admin.response.ThongKe;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_TKDT_Seller {
    Integer id;
    String fullname;
    String email;
    String phone;
    Boolean gender;
    String shopName;
    String avatar;
    boolean status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    Date createAt;
    Double DTshop;
    Double DTSan;
    Double Phi;
    Double loiNhuan;
}
