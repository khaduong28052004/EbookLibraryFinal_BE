package com.toel.dto.admin.response.ThongKe;

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
    String shopName;
    boolean status;
    Double DTshop;
    Double DTSan;
    Double Phi;
    Double loiNhuan;
}
