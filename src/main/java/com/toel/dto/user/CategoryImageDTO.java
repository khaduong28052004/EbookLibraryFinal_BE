package com.toel.dto.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryImageDTO {
    private String name;
    private Integer id;
    private List<String> imageUrls;
}
