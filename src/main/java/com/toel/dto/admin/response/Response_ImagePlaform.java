package com.toel.dto.admin.response;

import com.toel.model.CategoryImage;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_ImagePlaform {
    Integer id;
    String url;
    CategoryImage categoryImage;
}
