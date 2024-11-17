package com.toel.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter @Setter
public class RolePermissionDTO {

    private Integer id;
    private Integer role;
    private Integer permission;
    // Getters and Setters
}
