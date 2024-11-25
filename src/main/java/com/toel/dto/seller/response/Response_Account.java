package com.toel.dto.seller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Account {
    Integer id;
    String avatar;
    String fullname;
    String phone;
    String background;
    String shopName;
    Response_Address address;
}
