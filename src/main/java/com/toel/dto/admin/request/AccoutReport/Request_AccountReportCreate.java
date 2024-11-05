package com.toel.dto.admin.request.AccoutReport;

import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_AccountReportCreate {
    @NotBlank(message = "FIELD_REQUIRED")
    String title;
    @NotBlank(message = "FIELD_REQUIRED")
    Date resolve_at;
    @NotBlank(message = "FIELD_REQUIRED")
    String content;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer product;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer parent_id;
    @Min(value = 1, message = "FIELD_INVALID_ID")
    Integer account;
}
