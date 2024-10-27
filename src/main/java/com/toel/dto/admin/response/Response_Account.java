package com.toel.dto.admin.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Account {
    Integer id;
    String username;
    String password;
    String avatar;
    String fullname;
    boolean gender;
    String email;
    Date birthday;
    String phone;
    String background;
    String shopName;
    boolean status;
    String numberCitizenIdentification;
    String beforeCitizenIdentification;
    String afterCitizenIdentification;
}
