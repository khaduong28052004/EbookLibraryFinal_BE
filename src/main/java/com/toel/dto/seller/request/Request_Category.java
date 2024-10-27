package com.toel.dto.seller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_Category {
    Integer id;
    String name;
    Integer idParent;
}
