package com.toel.dto.admin.request.Account;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_AccountCreate {
    @NotBlank(message = "FIELD_REQUIRED")
    String username;
    @NotBlank(message = "FIELD_REQUIRED")
    @Length(min = 8, message = "FIELD_MIN_VALUE")
    String password;
    @NotBlank(message = "FIELD_REQUIRED")
    String fullname;
    @NotNull(message = "FIELD_REQUIRED")
    Boolean gender;
    @NotBlank(message = "FIELD_REQUIRED")
    String email;
    // @NotBlank(message = "FIELD_REQUIRED")
    Date birthday;
    @NotBlank(message = "FIELD_REQUIRED")
    String phone;
}
