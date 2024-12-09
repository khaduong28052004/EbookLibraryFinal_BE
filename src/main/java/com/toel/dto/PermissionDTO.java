package com.toel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {

    private Integer id;
    private String description;
    private String cotSlug;

    // Getters and Setters
}
