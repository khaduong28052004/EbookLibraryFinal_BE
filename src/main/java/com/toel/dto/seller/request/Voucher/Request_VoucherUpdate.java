package com.toel.dto.seller.request.Voucher;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_VoucherUpdate {
    @NotNull(message = "FIELD_REQUIRED")
    Integer id;
    @NotBlank(message = "FIELD_REQUIRED")
    String name;
    @NotBlank(message = "FIELD_REQUIRED")
    String note;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 0, message = "FIELD_MIN_VALUE")
    double minOrder;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1000, message = "FIELD_MIN_VALUE")
    double totalPriceOrder;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 0, message = "FIELD_MIN_VALUE")
    @Max(value = 100, message = "FIELD_MIN_VALUE")
    double sale;
    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 1, message = "FIELD_MIN_VALUE")
    int quantity;
    @NotNull(message = "FIELD_REQUIRED")
    boolean isDelete;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Future(message = "FIELD_INVALID_FUTURE_DATE")
    Date dateStart;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Future(message = "FIELD_INVALID_FUTURE_DATE")
    Date dateEnd;
    @NotNull(message = "FIELD_REQUIRED")
    Integer typeVoucher;
    @NotNull(message = "FIELD_REQUIRED")
    Integer account;
}
