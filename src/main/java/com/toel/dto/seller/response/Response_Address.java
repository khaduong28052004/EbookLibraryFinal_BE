package com.toel.dto.seller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Address {
    Integer id;
    boolean status;
    String phone;
    String fullNameAddress;
    String street;
    Integer province;
    Integer district;
    String wardCode;
}
