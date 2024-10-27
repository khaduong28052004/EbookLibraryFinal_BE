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
    @NotNull(message = "FIELD_REQUIRED")
    String username;
    @NotNull(message = "FIELD_REQUIRED")
    String password;
    @NotNull(message = "FIELD_REQUIRED")
    String fullname;
    @NotNull(message = "FIELD_REQUIRED")
    Boolean gender;
    @NotNull(message = "FIELD_REQUIRED")
    String email;
    Date birthday;
    @NotNull(message = "FIELD_REQUIRED")
    String phone;
}
