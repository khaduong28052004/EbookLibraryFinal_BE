package com.toel.dto.seller.request.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_ProductCreate {
    @NotNull(message = "FIELD_REQUIRED")
    double price;
    @NotNull(message = "FIELD_REQUIRED")
    double sale;
    @NotNull(message = "FIELD_REQUIRED")
    double weight;
    @NotBlank(message = "FIELD_REQUIRED")
    String name;
    @NotBlank(message = "FIELD_REQUIRED")
    String introduce;
    @NotBlank(message = "FIELD_REQUIRED")
    String writerName;
    @NotBlank(message = "FIELD_REQUIRED")
    String publishingCompany;
    @NotNull(message = "FIELD_REQUIRED")
    boolean isDelete;
    @NotNull(message = "FIELD_REQUIRED")
    Integer quantity;
    @NotNull(message = "FIELD_REQUIRED")
    boolean isActive;
    @NotNull(message = "FIELD_REQUIRED")
    Integer account;
    @NotNull(message = "FIELD_REQUIRED")
    Integer category;
    // @NotNull(message = "FIELD_REQUIRED")
    // List<Request_ImageProduct> imageProducts;
}
