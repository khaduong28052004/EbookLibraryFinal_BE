package com.toel.dto.admin.request.Account;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_AccountUpdate {
    @NotBlank(message = "FIELD_REQUIRED")
    String avatar;
    @NotBlank(message = "FIELD_REQUIRED")
    String background;
    @NotBlank(message = "FIELD_REQUIRED")
    String fullname;
    @NotNull(message = "FIELD_REQUIRED")
    Boolean gender;
    @NotBlank(message = "FIELD_REQUIRED")
    String email;
    @NotBlank(message = "FIELD_REQUIRED")
    Date birthday;
    @NotBlank(message = "FIELD_REQUIRED")
    String phone;
}
