package com.toel.dto.admin.request.Account;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_AccountCreateOTP {
    @NotBlank(message = "FIELD_REQUIRED")
    String username;
    @NotBlank(message = "FIELD_REQUIRED")
    @Length(min = 8, message = "FIELD_MIN_VALUE")
    String password;
    @NotBlank(message = "FIELD_REQUIRED")
    String fullname;
    @NotBlank(message = "FIELD_REQUIRED")
    String email;
    @NotBlank(message = "FIELD_REQUIRED")
    String phone;
    @NotBlank(message = "FIELD_REQUIRED")
    String method;

}
