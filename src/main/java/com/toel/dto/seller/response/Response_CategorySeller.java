package com.toel.dto.seller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_CategorySeller {
    Integer id;
    String name;
    Integer idParent;
    String nameDanhMuc;
    Boolean hasProducts;
}
