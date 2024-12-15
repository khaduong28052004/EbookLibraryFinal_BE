package com.toel.dto.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryImageDTO {
    private Integer id;
    private String name;
    private List<ImagePlatformDTO> images;
}
