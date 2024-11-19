package com.toel.dto.admin.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Account {
    Integer id;
    String username;
    String fullname;
    Boolean gender;
    String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    Date birthday;
    String phone;
    String avatar;
    String background;
    String shopName;
    boolean status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    Date createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    Date createAtSeller;
    String numberId;
    String beforeIdImage;
    String afterIdImage;
}
