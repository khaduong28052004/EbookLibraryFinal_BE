package com.toel.dto.admin.request.Account;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_AccountCreate {
    @NotNull(message = "username không được để trống.")
    String username;
    @NotNull(message = "password không được để trống.")
    String password;
    @NotNull(message = "fullname không được để trống.")
    String fullname;
    @NotNull(message = "giới tính không được để trống.")
    Boolean gender;
    @NotNull(message = "email không được để trống.")
    String email;
    Date birthday;
    @NotNull(message = "số điện thoại không được để trống.")
    String phone;
}
