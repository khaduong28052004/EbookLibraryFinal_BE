package com.toel.dto.seller.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Category {
    Integer id;
    String name;
    Integer idParent;
    Response_Account account;
}
