package com.toel.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Platform {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String policies;
}
