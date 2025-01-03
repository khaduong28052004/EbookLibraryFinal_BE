package com.toel.dto.admin.request.voucher;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_VoucherUpdate {
    Integer id;
    @NotBlank(message = "FIELD_REQUIRED")
    String name;
    @NotBlank(message = "FIELD_REQUIRED")
    String note;
    @NotNull(message = "FIELD_REQUIRED")
    double totalPriceOrder;
    @NotNull(message = "FIELD_REQUIRED")
    double sale;
    @NotNull(message = "FIELD_REQUIRED")
    int quantity;
    @NotNull(message = "FIELD_REQUIRED")
    boolean isDelete;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    Date dateStart;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    Date dateEnd;
    @NotNull(message = "FIELD_REQUIRED")
    Integer typeVoucher;
    @NotNull(message = "FIELD_REQUIRED")
    Integer account;
}
