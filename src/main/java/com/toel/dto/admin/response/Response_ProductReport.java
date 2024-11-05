package com.toel.dto.admin.response;

import java.util.Date;

import com.toel.model.Account;
import com.toel.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_ProductReport {
    Integer id;
    String title;
    boolean status;
    Date createAt;
    Date resolve_at;
    String content;
    Product product;
    Account account;
}
