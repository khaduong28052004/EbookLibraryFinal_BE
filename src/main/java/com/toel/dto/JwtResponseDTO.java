package com.toel.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtResponseDTO {
    private String accessToken;
    private Integer id_account;
    private String username;
    private String avatar;
    private String fullname;
    private String roles;
    private List<PermissionDTO> Permission;

}